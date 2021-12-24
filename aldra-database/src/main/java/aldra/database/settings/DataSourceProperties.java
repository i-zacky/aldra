package aldra.database.settings;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class DataSourceProperties {

  private String driverClassName;

  private String poolName;

  private String url;

  private String username;

  private String password;

  private int maximumPoolSize;

  private int minimumIdle;

  private int idleTimeout;

  private int connectionTimeout;

  private long leakDetectionThreshold;
}
