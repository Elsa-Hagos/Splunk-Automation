package safaricom.et.Splunk.Auto.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import safaricom.et.Splunk.Auto.Config.SecurityConfig;
import safaricom.et.Splunk.Auto.Enums.Frequency;
import safaricom.et.Splunk.Auto.Model.Recipient;
import safaricom.et.Splunk.Auto.Model.RecipientReport;
import safaricom.et.Splunk.Auto.Model.ReportType;
import safaricom.et.Splunk.Auto.Repo.RecipientRepo;
import safaricom.et.Splunk.Auto.Repo.RecipientReportRepo;
import safaricom.et.Splunk.Auto.Repo.ReportTypeRepo;
import java.util.List;
import java.util.Set;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class ReportTypeService {

    private final RecipientRepo recipientRepository;
    private final ReportTypeRepo reportTypeRepository;
    private final RecipientReportRepo recipientReportRepository;
    private final AuthenticationService authenticationService;

    @Autowired
    public ReportTypeService(ReportTypeRepo reportTypeRepository, RecipientRepo recipientRepository, RecipientReportRepo recipientReportRepository, AuthenticationService authenticationService) {
        this.reportTypeRepository = reportTypeRepository;
        this.recipientRepository = recipientRepository;
        this.recipientReportRepository = recipientReportRepository;
        this.authenticationService = authenticationService;
    }

    public ReportType createReportType(String query, String reportName, Set<String> recipientNames, Frequency frequency) {
        authenticationService.onUserActivity();
        SecurityConfig securityConfig = new SecurityConfig(authenticationService);
        securityConfig.checkLogin();
        Optional<ReportType> existingReportType = reportTypeRepository.findByReportName(reportName);
        if (existingReportType.isPresent()) {
            throw new IllegalArgumentException("ReportType with name " + reportName + " already exists.");
        }
        ReportType reportType = new ReportType();
        reportType.setQuery("search " +query);
        reportType.setFrequency(frequency);
        reportType.setReportName(reportName);
        reportType = reportTypeRepository.save(reportType);

         for (String recipientName : recipientNames) {
            Optional<Recipient> recipientOpt = recipientRepository.findByName(recipientName);
            if (recipientOpt.isPresent()) {
                Recipient recipient = recipientOpt.get();
                RecipientReport recipientReport = new RecipientReport();
                recipientReport.setRecipient(recipient);
                recipientReport.setReportType(reportType);
                recipientReportRepository.save(recipientReport);
                reportType.getReportRecipients().add(recipientReport);
            } else {
                throw new IllegalArgumentException("ReportType with name " + reportName + " does not exist.");
            }
        }

        return reportType;
    }

    public ReportType updateReportType(String reportName, ReportType updatedReportType) {
        authenticationService.onUserActivity();
        SecurityConfig securityConfig = new SecurityConfig(authenticationService);
        securityConfig.checkLogin();
        Optional<ReportType> reportTypeOptional = reportTypeRepository.findByReportName(reportName);
        if (reportTypeOptional.isPresent()) {
            ReportType existingReportType = reportTypeOptional.get();
            existingReportType.setQuery(updatedReportType.getQuery());
            existingReportType.setReportName(updatedReportType.getReportName());
            existingReportType.setFrequency(updatedReportType.getFrequency());
            return reportTypeRepository.save(existingReportType);
        } else {
            throw new IllegalArgumentException("ReportType with name " + reportName + " does not exist.");
        }
    }

    public void deleteReportType(String reportName) {
        authenticationService.onUserActivity();
        SecurityConfig securityConfig = new SecurityConfig(authenticationService);
        securityConfig.checkLogin();
        Optional<ReportType> reportTypeOptional = reportTypeRepository.findByReportName(reportName);
        if (reportTypeOptional.isPresent()) {
            ReportType reportType = reportTypeOptional.get();
            List<RecipientReport> recipientReports = recipientReportRepository.findByReportTypeId(reportType.getId());
            if (recipientReports.isEmpty()) {
                reportTypeRepository.deleteByReportName(reportName);
            } else {
                recipientReportRepository.deleteAll(recipientReports);
                reportTypeRepository.deleteByReportName(reportName);
            }
        } else {
            throw new IllegalArgumentException("ReportType with name " + reportName + " does not exist.");
        }
    }

    public Optional<ReportType> viewReportType(String reportName){
        authenticationService.onUserActivity();
        SecurityConfig securityConfig = new SecurityConfig(authenticationService);
        securityConfig.checkLogin();
        Optional<ReportType> reportTypeOptional = reportTypeRepository.findByReportName(reportName);
        if (reportTypeOptional.isPresent()) {
            return reportTypeRepository.findByReportName(reportName);
        }
    else {
        throw new IllegalArgumentException("ReportType with name " + reportName + " does not exist.");

    }}


    public List<ReportType> viewAllReportTypes() {
        authenticationService.onUserActivity();
        SecurityConfig securityConfig = new SecurityConfig(authenticationService);
        securityConfig.checkLogin();
        return reportTypeRepository.findAll();
    }

}
