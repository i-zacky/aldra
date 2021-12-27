package aldra.database.utils;

import aldra.database.settings.ClockSettings;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateTimeHelper {

  public static LocalDate today() {
    return LocalDate.now(ClockSettings.getClock());
  }

  public static LocalDateTime now() {
    return LocalDateTime.now(ClockSettings.getClock());
  }

  public static LocalDate toLocalDate(String dateAsString) {
    return toLocalDate(dateAsString, DateTimePattern.DATE.format());
  }

  public static LocalDate toLocalDate(String dateAsString, @NonNull DateTimePattern pattern) {
    return toLocalDate(dateAsString, pattern.format());
  }

  public static LocalDate toLocalDate(String dateAsString, @NonNull DateTimeFormatter with) {
    if (StringUtils.isEmpty(dateAsString)) {
      return null;
    }
    return LocalDate.parse(dateAsString, with);
  }

  public static LocalDateTime toLocalDateTime(String dateTimeAsString) {
    return toLocalDateTime(dateTimeAsString, DateTimePattern.DATETIME.format());
  }

  public static LocalDateTime toLocalDateTime(String dateTimeAsString, @NonNull DateTimePattern pattern) {
    return toLocalDateTime(dateTimeAsString, pattern.format());
  }

  public static LocalDateTime toLocalDateTime(String dateTimeAsString, @NonNull DateTimeFormatter with) {
    if (StringUtils.isEmpty(dateTimeAsString)) {
      return null;
    }
    return LocalDateTime.parse(dateTimeAsString, with);
  }

  public static String format(LocalDate source) {
    return format(source, DateTimePattern.DATE.format());
  }

  public static String format(LocalDate source, @NonNull DateTimePattern pattern) {
    return source.format(pattern.format());
  }

  public static String format(LocalDate source, @NonNull DateTimeFormatter with) {
    return source.format(with);
  }

  public static String format(LocalDateTime source) {
    return format(source, DateTimePattern.DATETIME.format());
  }

  public static String format(LocalDateTime source, @NonNull DateTimePattern pattern) {
    return source.format(pattern.format());
  }

  public static String format(LocalDateTime source, @NonNull DateTimeFormatter with) {
    return source.format(with);
  }
}
