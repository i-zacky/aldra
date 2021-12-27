package aldra.common.framework.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApplicationException extends RuntimeException {

  Object response;

  private ApplicationException() {
    super();
  }

  private ApplicationException(String message) {
    super(message);
  }

  private <T> ApplicationException(String message, T response) {
    super(message);
    this.response = response;
  }

  private ApplicationException(Throwable cause) {
    super(cause);
  }

  private ApplicationException(String message, Throwable cause) {
    super(message, cause);
  }

  private <T> ApplicationException(String message, Throwable cause, T response) {
    super(message, cause);
    this.response = response;
  }

  public static ApplicationException newInstance() {
    return new ApplicationException();
  }

  public static ApplicationException withMessage(String message) {
    return new ApplicationException(message);
  }

  public static ApplicationException withCause(Throwable cause) {
    return new ApplicationException(cause);
  }

  public static ApplicationException withCause(String message, Throwable cause) {
    return new ApplicationException(message, cause);
  }

  public static <T> ApplicationException withResponse(String message, T response) {
    return new ApplicationException(message, response);
  }

  public static <T> ApplicationException withResponse(String message, Throwable cause, T response) {
    return new ApplicationException(message, cause, response);
  }
}
