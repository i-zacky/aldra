package aldra.api.utils;

import aldra.api.framework.auth.AuthenticatedUser;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityContextHelper {

  public static Optional<AuthenticatedUser> getPrincipal() {
    return Optional.ofNullable(SecurityContextHolder.getContext()) //
        .map(SecurityContext::getAuthentication) //
        .map(Authentication::getPrincipal) //
        .map(o -> (AuthenticatedUser) o);
  }

  public static Optional<String> getUsername() {
    return Optional.ofNullable(SecurityContextHolder.getContext()) //
        .map(SecurityContext::getAuthentication) //
        .map(Authentication::getPrincipal) //
        .map(o -> (AuthenticatedUser) o) //
        .map(AuthenticatedUser::getUsername);
  }

  public static String getUsername(@Nullable String defaultIfNull) {
    return Optional.ofNullable(SecurityContextHolder.getContext()) //
        .map(SecurityContext::getAuthentication) //
        .map(Authentication::getPrincipal) //
        .map(o -> (AuthenticatedUser) o) //
        .map(AuthenticatedUser::getUsername) //
        .orElse(defaultIfNull);
  }
}
