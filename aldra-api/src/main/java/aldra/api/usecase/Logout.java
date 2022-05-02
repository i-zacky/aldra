package aldra.api.usecase;

import aldra.api.adapter.web.controller.LogoutApi;
import aldra.api.utils.SecurityContextHelper;
import aldra.common.framework.exception.ApplicationException;
import aldra.common.framework.exception.ValidationException;
import aldra.common.utils.CognitoHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class Logout implements LogoutApi {

  private final CognitoHelper cognitoHelper;

  @Override
  public ResponseEntity<Void> execute() {
    val username =
        SecurityContextHelper.getUsername() //
            .orElseThrow(() -> ValidationException.withMessage("not found username"));
    try {
      cognitoHelper.logout(username);
    } catch (Exception e) {
      log.info("failed to logout", e);
      throw ApplicationException.withMessage("failed to logout");
    }
    return ResponseEntity.ok().build();
  }
}
