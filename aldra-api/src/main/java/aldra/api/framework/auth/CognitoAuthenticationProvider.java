package aldra.api.framework.auth;

import aldra.api.adapter.web.dto.ErrorCode;
import aldra.common.utils.CognitoHelper;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.InvalidParameterException;
import com.amazonaws.services.cognitoidp.model.NotAuthorizedException;
import com.amazonaws.services.cognitoidp.model.PasswordResetRequiredException;
import com.amazonaws.services.cognitoidp.model.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class CognitoAuthenticationProvider extends DaoAuthenticationProvider {

  private final CognitoHelper cognitoHelper;

  @Override
  protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
    AdminInitiateAuthResult initiateAuthResult;
    try {
      initiateAuthResult = cognitoHelper.login(userDetails.getUsername(), (String) authentication.getCredentials());
    } catch (NotAuthorizedException | UserNotFoundException | InvalidParameterException e) {
      log.info("failed to initiate auth", e);
      throw new AuthException(ErrorCode.EAN0001_0001);
    } catch (PasswordResetRequiredException e) {
      log.info("password reset is required", e);
      throw new AuthException(ErrorCode.EAN0001_0003);
    }

    // ChallengeName = "NEW_PASSWORD_REQUIRED" is required change temporary password
    if (StringUtils.equals(initiateAuthResult.getChallengeName(), "NEW_PASSWORD_REQUIRED")) {
      throw new AuthException(ErrorCode.EAN0001_0002);
    }

    if (Objects.isNull(initiateAuthResult.getAuthenticationResult())) {
      throw new AuthException(ErrorCode.EAN0001_0001);
    }

    Optional.of(authentication) //
            .ifPresent(token -> {
              val result = initiateAuthResult.getAuthenticationResult();
              val response = LoginResponse.builder() //
                      .idToken(result.getIdToken()) //
                      .refreshToken(result.getRefreshToken()) //
                      .build();
              token.setDetails(response);
            });
  }
}
