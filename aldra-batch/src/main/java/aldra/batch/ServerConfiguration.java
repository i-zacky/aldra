package aldra.batch;

import aldra.database.settings.ClockSettings;
import aldra.database.settings.DataSourceSettings;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@ConfigurationPropertiesScan(
    basePackages = { //
      "aldra.batch", //
      "aldra.common", //
      "aldra.database", //
    })
@ImportAutoConfiguration
@ComponentScan(
    basePackages = { //
      "aldra.batch", //
      "aldra.common", //
      "aldra.database", //
    })
@EnableAsync
@Import({ //
  DataSourceSettings.class, //
  ClockSettings.class, //
})
public class ServerConfiguration {}
