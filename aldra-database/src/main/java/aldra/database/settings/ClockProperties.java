package aldra.database.settings;

import java.time.ZoneId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Configuration
@ConfigurationProperties(prefix = "application.clock")
public class ClockProperties {

  boolean fixed;

  String timeZone;

  public ZoneId getZoneId() {
    return ZoneId.of(timeZone);
  }
}
