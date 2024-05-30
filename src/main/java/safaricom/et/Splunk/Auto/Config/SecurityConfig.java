package safaricom.et.Splunk.Auto.Config;


import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import safaricom.et.Splunk.Auto.Service.AuthenticationService;

public class SecurityConfig {
    private AuthenticationService authenticationService;
    public SecurityConfig(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
    public void checkLogin() {
        if (!authenticationService.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized: User not logged in");
        }}
//    public  void checkLogin( ) {
//        com.splunk.Service service = authenticationService.getService();
//        if (service != null & service == service.logout()) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized: Invalid session token");
//        }
//    }

}