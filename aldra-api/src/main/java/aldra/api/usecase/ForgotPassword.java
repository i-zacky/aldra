package aldra.api.usecase;

import aldra.api.adapter.web.controller.ForgotPasswordApi;
import aldra.api.adapter.web.dto.ForgotPasswordRequest;
import aldra.common.framework.exception.ApplicationException;
import aldra.common.utils.CognitoHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ForgotPassword implements ForgotPasswordApi {

  private final CognitoHelper cognitoHelper;

  @Override
  public ResponseEntity<Void> execute(@RequestBody ForgotPasswordRequest request) {
    try {
      cognitoHelper.forgotPassword(request.getEmail());
      log.info("reset password email will send from Lambda Trigger. email={}", request.getEmail());
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      log.error("failed to send email.", e);
      throw ApplicationException.withMessage("failed to send email");
    }
  }
}
