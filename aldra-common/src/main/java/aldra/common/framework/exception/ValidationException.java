package aldra.common.framework.exception;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ValidationException extends RuntimeException {

  List<String> messages;

  private ValidationException() {
    super();
    messages = List.of();
  }

  private ValidationException(String message) {
    super(message);
    messages = List.of(message);
  }

  public static ValidationException newInstance() {
    return new ValidationException();
  }

  public static ValidationException withMessage(String message) {
    return new ValidationException(message);
  }

  public ValidationException addMessage(String message) {
    messages.add(message);
    return this;
  }
}
