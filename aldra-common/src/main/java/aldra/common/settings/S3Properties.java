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
@ConfigurationProperties(prefix = "aws.s3")
public class S3Properties {

    private String endpointUrl;

    private String region;

    private String dataBucket;

    private Long expirationSeconds;
}
