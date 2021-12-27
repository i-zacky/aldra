package aldra.api.framework.auth;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LoginResponse {

  String idToken;

  String refreshToken;
}
