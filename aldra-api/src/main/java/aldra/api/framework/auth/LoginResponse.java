package aldra.api.framework.auth;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LoginResponse {

  String status;

  String idToken;

  String refreshToken;
}
