package aldra.api.usecase;

import aldra.api.adapter.web.controller.ResetPasswordApi;
import aldra.api.adapter.web.dto.ResetPasswordRequest;
import aldra.common.framework.exception.ApplicationException;
import aldra.common.framework.exception.ValidationException;
import aldra.common.utils.CognitoHelper;
import com.amazonaws.services.cognitoidp.model.ExpiredCodeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ResetPassword implements ResetPasswordApi {

  private final CognitoHelper cognitoHelper;

  @Override
  public ResponseEntity<Void> execute(@RequestBody ResetPasswordRequest request) {
    try {
      cognitoHelper.confirmForgotPassword(request.getEmail(), request.getNewPassword(), request.getConfirmationCode());
      log.info("confirm forgot password was succeed. email={}", request.getEmail());
      return ResponseEntity.ok().build();
    } catch (ExpiredCodeException e) {
      log.error("confirmation code is expired.", e);
      throw ValidationException.withMessage("confirmation code is expired");
    } catch (Exception e) {
      log.error("failed to reset password.", e);
      throw ApplicationException.withMessage("failed to reset password");
    }
  }
}
