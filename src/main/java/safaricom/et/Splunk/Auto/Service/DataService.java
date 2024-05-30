package safaricom.et.Splunk.Auto.Service;

import com.splunk.*;
import safaricom.et.Splunk.Auto.Config.SplunkConfig;
import safaricom.et.Splunk.Auto.Enums.Frequency;
import safaricom.et.Splunk.Auto.Exceptions.EmailSendingException;
import safaricom.et.Splunk.Auto.Exceptions.FetchDataException;
import safaricom.et.Splunk.Auto.Exceptions.SplunkJobInterruptedException;
import safaricom.et.Splunk.Auto.Model.ReportHistory;
import safaricom.et.Splunk.Auto.Model.ReportType;
import safaricom.et.Splunk.Auto.Repo.ReportHistoryRepo;
import safaricom.et.Splunk.Auto.Repo.ReportTypeRepo;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@org.springframework.stereotype.Service
public class DataService {

    private static  ReportTypeRepo reportTypeRepo;

    private  static ReportHistoryRepo reportHistoryRepository ;

    private  static EmailService emailService;

    public DataService(ReportTypeRepo reportTypeRepo, ReportHistoryRepo reportHistoryRepository, EmailService emailService) {
        this.reportTypeRepo = reportTypeRepo;
        this.reportHistoryRepository = reportHistoryRepository;
        this.emailService = emailService;
    }

    public static void fetchDataByFrequency(Frequency frequency) throws FetchDataException, EmailSendingException {
    List<ReportType> reportTypes = reportTypeRepo.findByFrequency(frequency);
    for (ReportType reportType : reportTypes) {
        String query = reportType.getQuery();
        File excelFile = null;
        try {
            excelFile = fetchDataAndSave(query, frequency , reportType);
        } catch (IOException | SplunkJobInterruptedException e) {
            throw new FetchDataException("Failed to fetch data and save to Excel", e);

        }
        emailService.sendEmailWithAttachment(excelFile,  frequency);
    }
}
private static File fetchDataAndSave(String query, Frequency frequency, ReportType reportType) throws SplunkJobInterruptedException, IOException {

        Service service =  SplunkConfig.configureConnection();
        JobArgs jobArgs = new JobArgs();
        jobArgs.setExecutionMode(JobArgs.ExecutionMode.NORMAL);
        Job job = service.getJobs().create(query, jobArgs);
        while (!job.isDone()) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
               Thread.currentThread().interrupt();
                throw new SplunkJobInterruptedException("Interrupted while waiting for Splunk job to finish", e);
            }
        }

        StringBuilder dataBuilder = new StringBuilder();
        InputStream results = job.getResults();
        ResultsReaderXml resultsReader = new ResultsReaderXml(results);
        Map<String, String> event;

        while ((event = resultsReader.getNextEvent()) != null) {
            for (String value : event.values()) {
                dataBuilder.append(value).append(",");
            }
            dataBuilder.append("\n");
        }
        resultsReader.close();
        File excelFile = Conversion.toExcel(dataBuilder.toString().getBytes(), "Report.xlsx");
        byte[] excelBytes = Conversion.toByteArray(excelFile);
        ReportHistory reportHistory = new ReportHistory();
        reportHistory.setGeneratedDate(LocalDate.now());
        reportHistory.setExcelFile(excelBytes);
        reportHistory.setReportType(reportType);
        reportHistory.setFrequency(frequency);
        reportHistoryRepository.save(reportHistory);
        return excelFile;
    }
}





