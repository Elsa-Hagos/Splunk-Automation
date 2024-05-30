package safaricom.et.Splunk.Auto.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import safaricom.et.Splunk.Auto.Model.UserActivityLog;
import safaricom.et.Splunk.Auto.Service.UserActivityLogService;
import java.time.LocalDate;
import java.util.List;

@RestController
public class UserActivityLogController {
    @Autowired
    private UserActivityLogService userActivityLogService;
    @GetMapping("/UserLogs")
    public List<UserActivityLog> getUserActivityLogsByDate(@RequestParam("date") String dateString) {
        LocalDate date = LocalDate.parse(dateString);
        return userActivityLogService.getUserActivityLogsByDate(date);
    }
}
