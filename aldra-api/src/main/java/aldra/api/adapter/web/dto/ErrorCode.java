package aldra.api.adapter.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {

    // AuthN errors
    EAN0001_0001("EAN-0001-0001", "username or password is invalid"), //
    EAN0001_0002("EAN-0001-0002", "required change temporary password"), //
    EAN0001_0003("EAN-0001-0003", "required reset password"), //
    // AuthZ errors
    EAZ0001_0001("EAZ-0001-0001", "authorization token is empty"), //
    EAZ0001_0002("EAZ-0001-0002", "authorization token is expired"), //
    EAZ0001_0003("EAZ-0001-0003", "authorization token is invalid"), //
    EAZ0002_0001("EAZ-0002-0001", "permission denied"), //
    // validation errors
    EV0001_0001("EV-0001-0001", "validation error"), //
    // application errors
    EAP0001_0001("EAP-0001-0001", "application error"), //
    // system errors
    ES0000_0000("EAS-0000-0000", "system error"), //
  ;

  private final String code;

  private final String message;

  @JsonGetter
  public String code() {
    return this.code;
  }

  @JsonGetter
  public String message() {
    return this.message;
  }

  public static ErrorCode fromMessage(String message) {
    return Arrays.stream(values()) //
            .filter(v -> StringUtils.equals(v.message, message)) //
            .findFirst() //
            .orElse(null);
  }
}
