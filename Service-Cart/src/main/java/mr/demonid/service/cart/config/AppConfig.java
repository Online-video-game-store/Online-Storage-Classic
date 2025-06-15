package mr.demonid.service.cart.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "app.config")
public class AppConfig {
    private String claimUserId;
    private String cookieAnonId;
}
