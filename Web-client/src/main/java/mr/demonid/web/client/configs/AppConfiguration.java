package mr.demonid.web.client.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "app.config")
public class AppConfiguration {
    private String gatewayUrl;
    private String authServerUrl;
}
