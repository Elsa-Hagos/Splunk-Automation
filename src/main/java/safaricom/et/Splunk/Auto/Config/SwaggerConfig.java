package safaricom.et.Splunk.Auto.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class SwaggerConfig {
    @Bean
    public OpenAPI openAPIConfiguration() {
        return new OpenAPI()
                .info(new Info().title("Splunk-Automaton")
                        .version("1.0")
                        .description(" Swagger Documentation"));

    }

}

