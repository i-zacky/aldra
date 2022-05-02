package aldra.api.usecase;

import aldra.api.adapter.web.controller.ChangePasswordApi;
import aldra.api.adapter.web.dto.ChangePasswordRequest;
import aldra.api.adapter.web.dto.ChangePasswordResponse;
import aldra.api.utils.SecurityContextHelper;
import aldra.common.framework.exception.ApplicationException;
import aldra.common.framework.exception.ValidationException;
import aldra.common.utils.CognitoHelper;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.AuthenticationResultType;
import com.amazonaws.services.cognitoidp.model.NotAuthorizedException;
import com.amazonaws.services.cognitoidp.model.UserNotFoundException;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChangePassword implements ChangePasswordApi {

  private final CognitoHelper cognitoHelper;

  @Override
  public ResponseEntity<ChangePasswordResponse> execute(
      @RequestBody ChangePasswordRequest request) {
    // login current password
    val username =
        SecurityContextHelper.getUsername() //
            .orElseThrow(() -> ValidationException.withMessage("not found username"));
    AdminInitiateAuthResult authResult;
    try {
      authResult = cognitoHelper.login(username, request.getCurrentPassword());
    } catch (NotAuthorizedException | UserNotFoundException e) {
      log.error("failed to change password", e);
      throw ValidationException.withMessage("invalid current password");
    } catch (Exception e) {
      log.error("unexpected error occurred", e);
      throw ApplicationException.withMessage("failed to change password");
    }

    if (Objects.nonNull(authResult.getChallengeName())
        || Objects.isNull(authResult.getAuthenticationResult())) {
      throw ValidationException.withMessage("your password cannot change");
    }

    // change password
    try {
      cognitoHelper.changePassword( //
          authResult.getAuthenticationResult().getAccessToken(), //
          request.getCurrentPassword(), //
          request.getNewPassword() //
          );
      val optional = Optional.ofNullable(authResult.getAuthenticationResult());
      val response =
          new ChangePasswordResponse() //
              .idToken(optional.map(AuthenticationResultType::getIdToken).orElse(null)) //
              .refreshToken(optional.map(AuthenticationResultType::getRefreshToken).orElse(null));
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      log.error("unexpected error occurred", e);
      throw ApplicationException.withMessage("failed to change password");
    }
  }
}
