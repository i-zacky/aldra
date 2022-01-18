package aldra.api.framework.auth;

import aldra.api.adapter.web.dto.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.AuthenticationException;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Accessors(chain = true, fluent = true)
public class AuthException extends AuthenticationException {

  ErrorCode errorCode;

  public AuthException(ErrorCode errorCode) {
    super(errorCode.message());
    this.errorCode = errorCode;
  }

  public AuthException(ErrorCode errorCode, Throwable cause) {
    super(errorCode.message(), cause);
    this.errorCode = errorCode;
  }
}
