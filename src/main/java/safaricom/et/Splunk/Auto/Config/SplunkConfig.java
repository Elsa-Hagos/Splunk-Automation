package safaricom.et.Splunk.Auto.Config;

import com.splunk.Service;
import com.splunk.ServiceArgs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import java.util.HashMap;
import java.util.Map;
@ComponentScan(basePackages = "safaricom.et.Splunk.Auto")
@Configuration
@PropertySource("classpath:application.properties")
public class SplunkConfig {
    @Value("${SPLUNK_URL}")
    private String splunkUri;


    @Bean
    public Service splunkService() {

        Map<String, Object> connectionArgs = new HashMap<>();
        connectionArgs.put("host", splunkUri);
        return Service.connect(connectionArgs);
    }
    public static Service configureConnection() {
        ServiceArgs loginArgs = new ServiceArgs();
        loginArgs.setUsername(System.getenv("SPLUNK_USERNAME"));
        loginArgs.setPassword(System.getenv("SPLUNK_PASSWORD"));
        String splunkHost = System.getenv("SPLUNK_HOST");
        int splunkPort = Integer.parseInt(System.getenv("SPLUNK_PORT"));
        loginArgs.setHost(splunkHost);
        loginArgs.setPort(splunkPort);
        System.setProperty("https.protocols", "TLSv1.2");
        System.setProperty("https.cipherSuites", "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256");
        Service service = Service.connect(loginArgs);
        return service;
    }

}

