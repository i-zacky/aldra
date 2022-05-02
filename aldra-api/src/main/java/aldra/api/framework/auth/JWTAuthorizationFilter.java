package aldra.api.framework.auth;

import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Slf4j
public class JWTAuthorizationFilter extends AbstractPreAuthenticatedProcessingFilter {

  public JWTAuthorizationFilter(String pathPattern) {
    super();
    setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(pathPattern));
  }

  @Override
  protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
    return StringUtils.removeStart(request.getHeader("Authorization"), "Bearer ");
  }

  @Override
  protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
    return "";
  }
}
