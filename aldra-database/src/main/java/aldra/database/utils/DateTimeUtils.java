package aldra.database.utils;

import aldra.database.settings.ClockSettings;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateTimeUtils {

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
    return Optional.ofNullable(dateAsString) //
            .filter(s -> !s.isEmpty()) //
            .map(s -> LocalDate.parse(s, with)) //
            .orElse(null);
  }

  public static LocalDateTime toLocalDateTime(String dateTimeAsString) {
    return toLocalDateTime(dateTimeAsString, DateTimePattern.DATETIME.format());
  }

  public static LocalDateTime toLocalDateTime(String dateTimeAsString, @NonNull DateTimePattern pattern) {
    return toLocalDateTime(dateTimeAsString, pattern.format());
  }

  public static LocalDateTime toLocalDateTime(String dateTimeAsString, @NonNull DateTimeFormatter with) {
    return Optional.ofNullable(dateTimeAsString) //
            .filter(s -> !s.isEmpty()) //
            .map(s -> LocalDateTime.parse(s, with)) //
            .orElse(null);
  }

  public static String format(LocalDate source) {
    return format(source, DateTimePattern.DATE.format());
  }

  public static String format(LocalDate source, @NonNull DateTimePattern pattern) {
    return format(source, pattern.format());
  }

  public static String format(LocalDate source, @NonNull DateTimeFormatter with) {
    return Optional.ofNullable(source) //
            .map(d -> d.format(with)) //
            .orElse(null);
  }

  public static String format(LocalDateTime source) {
    return format(source, DateTimePattern.DATETIME.format());
  }

  public static String format(LocalDateTime source, @NonNull DateTimePattern pattern) {
    return format(source, pattern.format());
  }

  public static String format(LocalDateTime source, @NonNull DateTimeFormatter with) {
    return Optional.ofNullable(source) //
            .map(d -> d.format(with)) //
            .orElse(null);
  }
}
