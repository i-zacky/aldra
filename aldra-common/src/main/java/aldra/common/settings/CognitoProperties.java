package aldra.common.settings;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@NoArgsConstructor
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "aws.cognito")
public class CognitoProperties {

  private String region;

  private String poolId;

  private String clientId;

  private String issuer;

  private String jwkUrl;
}
