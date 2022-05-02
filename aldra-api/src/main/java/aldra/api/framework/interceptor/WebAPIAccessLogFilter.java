package aldra.api.framework.interceptor;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.ServletContextRequestLoggingFilter;

@Slf4j
public class WebAPIAccessLogFilter extends ServletContextRequestLoggingFilter {

  private static final Set<String> LOGGING_HEADERS =
      ImmutableSet.<String>builder() //
          .add("referer") //
          .add("origin") //
          .add("content-type") //
          .add("content-length") //
          .add("user-agent") //
          .build();

  public WebAPIAccessLogFilter() {
    setHeaderPredicate(LOGGING_HEADERS::contains);
    setIncludeQueryString(true);
    setIncludeHeaders(true);
    setIncludeClientInfo(true);
    setBeforeMessagePrefix("[Before] ");
    setBeforeMessageSuffix(StringUtils.EMPTY);
    setAfterMessagePrefix("[After] ");
    setAfterMessageSuffix(StringUtils.EMPTY);
  }

  @Override
  protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
    // ignore health check endpoint
    return StringUtils.equals("/actuator/health", request.getRequestURI());
  }

  @Override
  protected void beforeRequest(@NonNull HttpServletRequest request, @NonNull String message) {
    log.info(message);
  }

  @Override
  protected void afterRequest(@NonNull HttpServletRequest request, @NonNull String message) {
    log.info(message);
  }
}
