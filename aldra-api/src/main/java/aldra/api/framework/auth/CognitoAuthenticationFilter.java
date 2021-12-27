package aldra.api.framework.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class CognitoAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

  public CognitoAuthenticationFilter(String pathPattern, String httpMethod) {
    super(new AntPathRequestMatcher(pathPattern, httpMethod));
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
    val mapper = new ObjectMapper();
    val dto = mapper.readValue(request.getInputStream(), LoginRequest.class);
    val token = new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());
    token.setDetails(authenticationDetailsSource.buildDetails(request));
    return getAuthenticationManager().authenticate(token);
  }
}
