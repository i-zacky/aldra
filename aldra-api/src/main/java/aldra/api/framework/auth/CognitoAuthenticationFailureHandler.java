package aldra.api.framework.auth;

import aldra.api.adapter.web.dto.ErrorCode;
import aldra.api.adapter.web.dto.ExceptionResponseBase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class CognitoAuthenticationFailureHandler implements AuthenticationFailureHandler {

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
    ErrorCode errorCode = null;
    if (exception instanceof AuthException) {
      errorCode = ((AuthException) exception).errorCode();
    } else {
      val fromMessage = ErrorCode.fromMessage(exception.getMessage());
      if (Objects.nonNull(fromMessage)) {
        errorCode = fromMessage;
      } else {
        errorCode = ErrorCode.EAS0000_0000;
      }
    }

    val res = new ExceptionResponseBase(errorCode);
    val mapper = new ObjectMapper();
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setHeader("Content-Type", "application/json");
    response.getWriter().write(mapper.writeValueAsString(res));
  }
}
