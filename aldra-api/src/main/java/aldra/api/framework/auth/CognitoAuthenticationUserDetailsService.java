package aldra.api.framework.auth;

import aldra.api.adapter.web.dto.ErrorCode;
import aldra.common.utils.CognitoHelper;
import com.amazonaws.services.cognitoidp.model.UserType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Slf4j
@RequiredArgsConstructor
public class CognitoAuthenticationUserDetailsService implements UserDetailsService {

  private final CognitoHelper cognitoHelper;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    val users = cognitoHelper.getUserByEmail(username).getUsers();

    if (users.stream().filter(UserType::getEnabled).count() > 1) {
      log.error("retrieve multiple user: {}", username);
      throw new AuthException(ErrorCode.EAN0001_0001);
    }

    return users.stream() //
            .filter(UserType::getEnabled) //
            .findFirst() //
            .map(type -> AuthenticatedUser.authenticated(type.getUsername())) //
            .orElseThrow(() -> new AuthException(ErrorCode.EAN0001_0001));
  }
}
