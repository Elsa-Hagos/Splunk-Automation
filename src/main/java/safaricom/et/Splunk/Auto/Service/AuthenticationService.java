package safaricom.et.Splunk.Auto.Service;
import com.splunk.HttpException;
import com.splunk.Service;
import com.splunk.ServiceArgs;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@org.springframework.stereotype.Service
@Data
public class AuthenticationService {
    private Service service;
    private String sessionToken;
    private String username;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final int INACTIVITY_LIMIT = 300;
    private ScheduledFuture<?> logoutTask;


        public ResponseEntity<String> login(String username, String password) {
            ServiceArgs loginArgs = new ServiceArgs();
            loginArgs.setUsername(username);
            loginArgs.setPassword(password);
            String splunkHost = System.getenv("SPLUNK_HOST");
            int splunkPort = Integer.parseInt(System.getenv("SPLUNK_PORT"));
            loginArgs.setHost(splunkHost);
            loginArgs.setPort(splunkPort);
            try {
                System.setProperty("https.protocols", "TLSv1.2");
                System.setProperty("https.cipherSuites", "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256");
                this.service = Service.connect(loginArgs);
                this.sessionToken = UUID.randomUUID().toString();
                return ResponseEntity.ok("Login successful");
            } catch (HttpException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Invalid credentials");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
            } finally {
                if (logoutTask != null) {
                    logoutTask.cancel(false);
                }
                logoutTask = scheduler.schedule(this::logout, INACTIVITY_LIMIT, TimeUnit.SECONDS);
            }
        }

        public ResponseEntity<String> logout() {
            if (this.service != null ) {
                try {
                    this.service.logout();
                    this.service = null;
                    this.sessionToken = null;
                    return ResponseEntity.ok("Logout successful");
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error during logout");
                } finally {
                    if (logoutTask != null) {
                        logoutTask.cancel(false);
                    }
                }
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Service not initialized. Please login first.");
            }
        }

        public boolean isAuthenticated() {
            return service != null && sessionToken != null;
        }

        public void onUserActivity() {
            if (logoutTask != null) {
                logoutTask.cancel(false);
            }

            logoutTask = scheduler.schedule(this::logout, INACTIVITY_LIMIT, TimeUnit.SECONDS);
        }
    }


