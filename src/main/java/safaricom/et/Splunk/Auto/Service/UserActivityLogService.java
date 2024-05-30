package safaricom.et.Splunk.Auto.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import safaricom.et.Splunk.Auto.Config.SecurityConfig;
import safaricom.et.Splunk.Auto.Model.UserActivityLog;
import safaricom.et.Splunk.Auto.Repo.UserActivityLogRepo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.List;

@Service
public class UserActivityLogService {
@Autowired
    private   UserActivityLogRepo userActivityLogRepository;
private final AuthenticationService authenticationService;

    public UserActivityLogService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public void logActivity(String  userName, String activityType) {
        UserActivityLog log = new UserActivityLog();
        log.setUserName(userName);
        log.setActivityType(activityType);
        log.setTimestamp(LocalDateTime.now());
        userActivityLogRepository.save(log);
    }
    public List<UserActivityLog> getUserActivityLogsByDate(LocalDate date) {
        authenticationService.onUserActivity();
        SecurityConfig securityConfig = new SecurityConfig(authenticationService);
        securityConfig.checkLogin();
        LocalDateTime startDate = date.atStartOfDay();
        LocalDateTime endDate = date.atTime(23, 59, 59);
        return userActivityLogRepository.findByTimestampBetween(startDate, endDate);
    }
}

