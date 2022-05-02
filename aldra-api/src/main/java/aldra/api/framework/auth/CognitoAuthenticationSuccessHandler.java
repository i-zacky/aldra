package aldra.api.framework.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Slf4j
@RequiredArgsConstructor
public class CognitoAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {
    val mapper = new ObjectMapper();
    response.setStatus(HttpServletResponse.SC_OK);
    response.setHeader("Content-Type", "application/json");
    response.getWriter().write(mapper.writeValueAsString(authentication.getDetails()));
  }
}
