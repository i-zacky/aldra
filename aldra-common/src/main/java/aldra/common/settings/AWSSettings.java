package aldra.common.settings;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.EC2ContainerCredentialsProviderWrapper;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Slf4j
@Getter
@Configuration
@RequiredArgsConstructor
public class AWSSettings {

  private final AWSProperties general;

  private final S3Properties s3;

  private final CognitoProperties cognito;

  private final Environment environment;

  public AWSCredentials getCredentials() {
    val isLocal =
        Arrays.stream(environment.getActiveProfiles()) //
            .anyMatch(profile -> StringUtils.containsAny(profile, "local", "ut"));
    if (isLocal) {
      val accessKey = general.getAccessKeyId();
      val secretAccessKey = general.getSecretAccessKey();
      log.info(
          "loaded environment variables. AWS_ACCESS_KEY_ID={}, AWS_SECRET_ACCESS_KEY={}",
          accessKey,
          secretAccessKey);
      return new BasicAWSCredentials(accessKey, secretAccessKey);
    } else {
      val wrapper = new EC2ContainerCredentialsProviderWrapper();
      return wrapper.getCredentials();
    }
  }
}
