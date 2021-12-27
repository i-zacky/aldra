package aldra.api.framework.auth;

import aldra.common.settings.AWSSettings;
import com.auth0.jwk.GuavaCachedJwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class JWTAuthorizationUserDetailsService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

  private final AWSSettings awsSettings;

  @Override
  public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
    if (Objects.isNull(token.getPrincipal())) {
      throw new BadCredentialsException("JWT token is empty");
    }

    try {
      val decoded = JWT.decode((String) token.getPrincipal());
      val provider = new GuavaCachedJwkProvider(new UrlJwkProvider(new URL(awsSettings.getCognito().getJwkUrl())));
      val jwk = provider.get(decoded.getKeyId());
      val algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
      val verifier = JWT.require(algorithm) //
              .withIssuer(awsSettings.getCognito().getIssuer()) //
              .build();
      val verified = verifier.verify(decoded);

      return AuthenticatedUser.authenticated(verified.getClaim("cognito:username").asString());
    } catch (Exception e) {
      log.error("invalid jwt token", e);
      throw new BadCredentialsException("bad credentials");
    }
  }
}
