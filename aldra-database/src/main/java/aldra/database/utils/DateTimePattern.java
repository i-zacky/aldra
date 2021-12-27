package aldra.database.utils;

import lombok.AllArgsConstructor;

import java.time.format.DateTimeFormatter;

@AllArgsConstructor
public enum DateTimePattern {

    DATE(DateTimeFormatter.ofPattern("yyyy-MM-dd")), // yyyy-MM-dd
    DATE_NO_HYPHEN(DateTimeFormatter.ofPattern("yyyyMMdd")), // yyyyMMdd
    DATE_SLASH(DateTimeFormatter.ofPattern("yyyy/MM/dd")), // yyyy/MM/dd
    DATETIME(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), // yyyy-MM-dd HH:mm:ss
    DATETIME_SEPARATE_COMMA(DateTimeFormatter.ofPattern("yyyyMMdd;HHmmss")), // yyyyMMdd;HHmmss
    DATETIME_NO_SEPARATE(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")), // yyyyMMddHHmmss
    YEAR_MONTH(DateTimeFormatter.ofPattern("yyyyMM")); // yyyyMM

  private final DateTimeFormatter formatter;

  public DateTimeFormatter format() {
    return formatter;
  }
}
