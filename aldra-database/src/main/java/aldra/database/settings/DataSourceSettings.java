package aldra.database.settings;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@MapperScan(basePackages = { //
    "aldra.database.domain.repository", //
})
public class DataSourceSettings {

  @Bean(name = "dataSourceProperties")
  @ConfigurationProperties(prefix = "spring.datasource")
  public DataSourceProperties dataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean(name = "dataSource")
  public DataSource dataSource(@Qualifier("dataSourceProperties") final DataSourceProperties props) {
    HikariConfig config = new HikariConfig();
    config.setDriverClassName(props.getDriverClassName());
    config.setJdbcUrl(props.getUrl());
    config.setUsername(props.getUsername());
    config.setPassword(props.getPassword());
    config.setPoolName(props.getPoolName());
    config.setMaximumPoolSize(props.getMaximumPoolSize());
    config.setMinimumIdle(props.getMaximumPoolSize());
    config.setIdleTimeout(props.getIdleTimeout());
    config.setConnectionTimeout(props.getConnectionTimeout());
    config.setLeakDetectionThreshold(props.getLeakDetectionThreshold());
    return new HikariDataSource(config);
  }
}
