package safaricom.et.Splunk.Auto.Service;


import org.springframework.stereotype.Service;
import safaricom.et.Splunk.Auto.Config.SecurityConfig;
import safaricom.et.Splunk.Auto.Enums.Frequency;
import safaricom.et.Splunk.Auto.Model.ReportHistory;
import safaricom.et.Splunk.Auto.Repo.ReportHistoryRepo;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;



@Service
public class ReportHistoryService {

    private static ReportHistoryRepo reportHistoryRepo;
    private static AuthenticationService authenticationService;

    public ReportHistoryService(ReportHistoryRepo reportHistoryRepo , AuthenticationService authenticationService) {
        this.reportHistoryRepo = reportHistoryRepo;
        this.authenticationService = authenticationService;
    }

    public Optional<byte[]> getReportHistoryFile(Long reportId) {
        authenticationService.onUserActivity();
        SecurityConfig securityConfig = new SecurityConfig(authenticationService);
        securityConfig.checkLogin();
        Optional
                <ReportHistory> optionalReportHistory = reportHistoryRepo.findById(reportId);
        if (optionalReportHistory.isPresent()) {
            ReportHistory reportHistory = optionalReportHistory.get();
            byte[] excelData = reportHistory.getExcelFile();
            return Optional.of(excelData);
        }
        return Optional.empty();
    }
    public static List<ReportHistory> getAllReportHistory() {
        authenticationService.onUserActivity();
        SecurityConfig securityConfig = new SecurityConfig(authenticationService);
        securityConfig.checkLogin();
        return reportHistoryRepo.findAll();
    }

    public List<ReportHistory> getByFrequency(Frequency frequency) {
        authenticationService.onUserActivity();
        SecurityConfig securityConfig = new SecurityConfig(authenticationService);
        securityConfig.checkLogin();
        return reportHistoryRepo.findByFrequency(frequency);
    }
    public List<ReportHistory> getReportsByDateRange(LocalDate fromDate, LocalDate toDate) {
        authenticationService.onUserActivity();
        SecurityConfig securityConfig = new SecurityConfig(authenticationService);
        securityConfig.checkLogin();
        return reportHistoryRepo.findByGeneratedDateBetween(fromDate, toDate);
    }



}



