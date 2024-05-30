package safaricom.et.Splunk.Auto.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import safaricom.et.Splunk.Auto.Enums.Frequency;
import safaricom.et.Splunk.Auto.Model.ReportHistory;
import safaricom.et.Splunk.Auto.Repo.ReportHistoryRepo;
import safaricom.et.Splunk.Auto.Service.ReportHistoryService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/Report-History")
public class ReportHistoryController {
    @Autowired
    private ReportHistoryRepo reportHistoryRepo;
    @Autowired
    private ReportHistoryService reportHistoryService;
    @GetMapping("/DownloadReport/{reportId}")
    public ResponseEntity<byte[]> downloadReport(@PathVariable Long reportId) {
        Optional<byte[]> optionalReportBytes = reportHistoryService.getReportHistoryFile(reportId);
        if (optionalReportBytes.isPresent()) {
            byte[] reportBytes = optionalReportBytes.get();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", "report.xlsx");
            return new ResponseEntity<>(reportBytes, headers, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @GetMapping
    public List<ReportHistory> getAllReportHistory() {
        List<ReportHistory> reportHistories = reportHistoryService.getAllReportHistory();
        for (ReportHistory reportHistory : reportHistories) {
            reportHistory.setExcelFile(null);
        }
        return reportHistories;
    }
    @GetMapping("/frequency/{frequency}")
    public ResponseEntity<List<ReportHistory>> getByFrequency(@PathVariable Frequency frequency) {
        List<ReportHistory> reportHistories = reportHistoryService.getByFrequency(frequency);
        for (ReportHistory reportHistory : reportHistories) {
            reportHistory.setExcelFile(null);
        }
        return new ResponseEntity<>(reportHistories, HttpStatus.OK);
    }
    @GetMapping("/dateRange")
    public ResponseEntity<List<ReportHistory>> getReportsByDateRange(
            @RequestParam("fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam("toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        List<ReportHistory> reportHistories = reportHistoryService.getReportsByDateRange(fromDate, toDate);
        for (ReportHistory reportHistory : reportHistories) {
            reportHistory.setExcelFile(null);
        }
        return new ResponseEntity<>(reportHistories, HttpStatus.OK);
    }


}
