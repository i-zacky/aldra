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
@ConfigurationProperties(prefix = "aws")
public class AWSProperties {

    private String defaultRegion;

    private String accessKeyId;

    private String secretAccessKey;
}
