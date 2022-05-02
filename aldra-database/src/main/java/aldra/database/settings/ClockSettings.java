package aldra.database.settings;

import aldra.database.domain.entity.system.gen.FixedClock;
import aldra.database.domain.repository.system.FixedClockMapper;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.TimeZone;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Configuration
@EnableScheduling
public class ClockSettings {

  private final FixedClockMapper fixedClockMapper;

  private final ClockProperties clockProperties;

  @Getter private static Clock clock;

  public ClockSettings(FixedClockMapper fixedClockMapper, ClockProperties clockProperties) {
    this.fixedClockMapper = fixedClockMapper;
    this.clockProperties = clockProperties;
    clock = Clock.system(clockProperties.getZoneId());
    TimeZone.setDefault(TimeZone.getTimeZone(clockProperties.getZoneId()));
  }

  @Scheduled(fixedDelay = 300000)
  public void reloadFixedClock() {
    if (!clockProperties.isFixed()) {
      return;
    }
    fixedClockMapper
        .loadBaseTime() //
        .map(FixedClock::getBaseTime) //
        .ifPresentOrElse(
            baseTime -> {
              if (!LocalDateTime.now(clock).isEqual(baseTime)) {
                log.info(
                    "Running on fixed clock mode. Load fixed datetime: {}, TimeZone: {}",
                    baseTime,
                    clockProperties.getTimeZone());
                clock =
                    Clock.fixed(
                        baseTime.atZone(clockProperties.getZoneId()).toInstant(),
                        clockProperties.getZoneId());
              }
            },
            () -> {
              log.info(
                  "Running on fixed clock mode. Failed to load datetime. TimeZone: {}",
                  clockProperties.getTimeZone());
              clock = Clock.system(clockProperties.getZoneId());
            });
  }
}
