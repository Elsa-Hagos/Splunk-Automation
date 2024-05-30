package safaricom.et.Splunk.Auto.Service;


import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import safaricom.et.Splunk.Auto.Enums.Frequency;
import safaricom.et.Splunk.Auto.Model.Recipient;
import safaricom.et.Splunk.Auto.Model.ReportType;
import java.util.Optional;
import java.util.Set;


@Aspect
@Component
public class LoggingAspect {
@Autowired
    private  UserActivityLogService userActivityLogService;
@Autowired
    private  AuthenticationService authenticationService;
    public LoggingAspect(UserActivityLogService userActivityLogService , AuthenticationService authenticationService) {
        this.userActivityLogService = userActivityLogService;
        this.authenticationService =  authenticationService;
    }
    @AfterReturning(pointcut = "execution(* safaricom.et.Splunk.Auto.Service.AuthenticationService.login(..)) && args(username, ..)", returning = "response")
    public void logAfterLogin(String username, ResponseEntity<String> response) {
        if (response.getStatusCode() == HttpStatus.OK) {
            userActivityLogService.logActivity(username, "LOGIN");
        } else if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            userActivityLogService.logActivity(username, "INVALID_LOGIN_ATTEMPT");
        } else if (response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
            userActivityLogService.logActivity(username, "LOGIN_ERROR_INTERNAL_SERVER_ERROR");
        }}
    @AfterReturning("execution(* safaricom.et.Splunk.Auto.Service.AuthenticationService.logout())")
    public void logAfterLogout( ) {
        userActivityLogService.logActivity("LOGOUT",  "LOGOUT");}
    @AfterReturning("execution(* safaricom.et.Splunk.Auto.Service.RecipientService.getAllRecipients())")
    public void logAfterGetAllRecipients() {
        String username = authenticationService.getUsername();
         userActivityLogService.logActivity(username, "GET_ALL_RECIPIENTS");}
    @AfterReturning(pointcut = "execution(* safaricom.et.Splunk.Auto.Service.RecipientService.getRecipientByName(..)) && args(name)", returning = "recipient")
    public void logAfterGetRecipientByName(String name, Recipient recipient) {
        String username = authenticationService.getUsername();
        if (recipient != null) {
                userActivityLogService.logActivity(username, "GET_RECIPIENT_BY_NAME: " + name);
            } else {
                userActivityLogService.logActivity(username, "GET_RECIPIENT_BY_NAME_FAILED: " + name);
            }
        }

    @AfterReturning("execution(* safaricom.et.Splunk.Auto.Service.RecipientService.createRecipient())")
    public void logAfterCreateRecipient() {
        String username = authenticationService.getUsername();
        userActivityLogService.logActivity(username, "CREATE_RECIPIENT:");
    }
    @AfterReturning("execution(* safaricom.et.Splunk.Auto.Service.RecipientService.updateRecipient(..)) && args(name, updatedRecipient)")
    public void logAfterUpdateRecipient(String name, Recipient updatedRecipient) {
        String username = authenticationService.getUsername();
        userActivityLogService.logActivity(username, "UPDATE_RECIPIENT: " + name);
    }

    @AfterReturning("execution(* safaricom.et.Splunk.Auto.Service.RecipientService.deleteRecipient(String)) && args(name)")
    public void logAfterDeleteRecipient(String name) {
        String username = authenticationService.getUsername();
        userActivityLogService.logActivity(username, "DELETE_RECIPIENT: " + name);
    }

    @AfterReturning("execution(* safaricom.et.Splunk.Auto.Service.ReportTypeService.createReportType(..)) && args(query, reportName, recipientNames, frequency)")
    public void logAfterCreateReportType(String query, String reportName, Set<String> recipientNames, Frequency frequency) {
        String username = authenticationService.getUsername();
        userActivityLogService.logActivity(username, "CREATE_REPORT_TYPE: " + reportName);
    }

    @AfterReturning("execution(* safaricom.et.Splunk.Auto.Service.ReportTypeService.updateReportType(..)) && args(reportName, updatedReportType)")
    public void logAfterUpdateReportType(String reportName, ReportType updatedReportType) {
        String username = authenticationService.getUsername();
        userActivityLogService.logActivity(username, "UPDATE_REPORT_TYPE: " + reportName);
    }

    @AfterReturning("execution(* safaricom.et.Splunk.Auto.Service.ReportTypeService.deleteReportType(String)) && args(reportName)")
    public void logAfterDeleteReportType(String reportName) {
        String username = authenticationService.getUsername();
        userActivityLogService.logActivity(username, "DELETE_REPORT_TYPE: " + reportName);
    }

    @AfterReturning("execution(* safaricom.et.Splunk.Auto.Service.ReportTypeService.viewReportType(String)) && args(reportName)")
    public void logAfterViewReportType(String reportName) {
        String username = authenticationService.getUsername();
        userActivityLogService.logActivity(username, "VIEW_REPORT_TYPE: " + reportName);
    }

    @AfterReturning("execution(* safaricom.et.Splunk.Auto.Service.ReportTypeService.viewAllReportTypes())")
    public void logAfterViewAllReportTypes() {
        String username = authenticationService.getUsername();
        userActivityLogService.logActivity(username, "VIEW_ALL_REPORT_TYPES");
    }
    @AfterReturning(
            pointcut = "execution(* safaricom.et.Splunk.Auto.Service.ReportHistoryService.getReportHistoryFile(Long)) && args(reportId)",
            returning = "result")
    public void logAfterGetReportHistoryFile(Long reportId, Optional<byte[]> result) {
        String username = authenticationService.getUsername();
        if (result.isPresent()) {
            userActivityLogService.logActivity(username, "VIEW_REPORT_HISTORY_FILE: Report ID " + reportId);
        } else {
            userActivityLogService.logActivity(username, "VIEW_REPORT_HISTORY_FILE_FAILED: Report ID " + reportId);
        }
    }

    @AfterReturning("execution(* safaricom.et.Splunk.Auto.Service.ReportHistoryService.getAllReportHistory())")
    public void logAfterGetAllReportHistory() {
        String username = authenticationService.getUsername();
        userActivityLogService.logActivity(username, "VIEW_ALL_REPORT_HISTORY");
    }
}









