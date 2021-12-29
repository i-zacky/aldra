package aldra.api.usecase;

import aldra.api.adapter.web.controller.ChangeTemporaryPasswordApi;
import aldra.api.adapter.web.dto.ChangeTemporaryPasswordRequest;
import aldra.api.adapter.web.dto.ChangeTemporaryPasswordResponse;
import aldra.common.framework.exception.ApplicationException;
import aldra.common.framework.exception.ValidationException;
import aldra.common.utils.CognitoHelper;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.AdminRespondToAuthChallengeResult;
import com.amazonaws.services.cognitoidp.model.AuthenticationResultType;
import com.amazonaws.services.cognitoidp.model.NotAuthorizedException;
import com.amazonaws.services.cognitoidp.model.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChangeTemporaryPassword implements ChangeTemporaryPasswordApi {

  private final CognitoHelper cognitoHelper;

  @Override
  public ResponseEntity<ChangeTemporaryPasswordResponse> execute(@RequestBody ChangeTemporaryPasswordRequest request) {
    // login by temporary password
    AdminInitiateAuthResult authResult;
    try {
      authResult = cognitoHelper.login(request.getEmail(), request.getTemporaryPassword());
    } catch (NotAuthorizedException | UserNotFoundException e) {
      log.error("failed to change temporary password", e);
      throw ValidationException.withMessage("invalid email or temporary password");
    } catch (Exception e) {
      log.error("unexpected error occurred", e);
      throw ApplicationException.withMessage("failed to change temporary password");
    }

    if (!StringUtils.equals(authResult.getChallengeName(), "NEW_PASSWORD_REQUIRED")) {
      throw ValidationException.withMessage("your temporary password already changed");
    }

    // change temporary password
    try {
      AdminRespondToAuthChallengeResult result = cognitoHelper.changeTemporaryPassword( //
              authResult.getChallengeName(), //
              authResult.getSession(), //
              request.getEmail(), //
              request.getNewPassword());

      val optional = Optional.ofNullable(result.getAuthenticationResult());
      val response = new ChangeTemporaryPasswordResponse() //
              .idToken(optional.map(AuthenticationResultType::getIdToken).orElse(null)) //
              .refreshToken(optional.map(AuthenticationResultType::getRefreshToken).orElse(null));
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      log.error("unexpected error occurred", e);
      throw ApplicationException.withMessage("failed to change temporary password");
    }
  }
}
