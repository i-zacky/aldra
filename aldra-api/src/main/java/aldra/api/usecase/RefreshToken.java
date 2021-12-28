package aldra.api.usecase;

import aldra.api.adapter.web.controller.RefreshTokenApi;
import aldra.api.adapter.web.dto.RefreshTokenRequest;
import aldra.api.adapter.web.dto.RefreshTokenResponse;
import aldra.common.utils.CognitoHelper;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.AuthenticationResultType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RefreshToken implements RefreshTokenApi {

  private final CognitoHelper cognitoHelper;

  @Override
  public ResponseEntity<RefreshTokenResponse> execute(@RequestBody RefreshTokenRequest request) {
    AdminInitiateAuthResult initiateAuthResult;
    try {
      initiateAuthResult = cognitoHelper.refreshToken(request.getRefreshToken());
    } catch (Exception e) {
      log.error("failed to refresh token", e);
      throw new CredentialsExpiredException("failed to refresh token");
    }

    val response = new RefreshTokenResponse() //
            .idToken(Optional.ofNullable(initiateAuthResult.getAuthenticationResult()) //
                    .map(AuthenticationResultType::getIdToken) //
                    .orElse(null));
    return ResponseEntity.ok(response);
  }
}
