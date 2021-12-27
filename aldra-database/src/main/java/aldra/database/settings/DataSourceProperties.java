package aldra.database.settings;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DataSourceProperties {

  String driverClassName;

  String poolName;

  String url;

  String username;

  String password;

  int maximumPoolSize;

  int minimumIdle;

  int idleTimeout;

  int connectionTimeout;

  long leakDetectionThreshold;
}
