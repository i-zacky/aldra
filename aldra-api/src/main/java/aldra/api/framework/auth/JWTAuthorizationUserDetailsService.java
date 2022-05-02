package aldra.api.framework.auth;

import aldra.api.adapter.web.dto.ErrorCode;
import aldra.common.settings.AWSSettings;
import aldra.database.domain.repository.user.AuthorityMapper;
import com.auth0.jwk.GuavaCachedJwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

@Slf4j
@RequiredArgsConstructor
public class JWTAuthorizationUserDetailsService
    implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

  private final AWSSettings awsSettings;

  private final AuthorityMapper authorityMapper;

  @Override
  public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token)
      throws UsernameNotFoundException {
    if (Objects.isNull(token.getPrincipal())) {
      throw new AuthException(ErrorCode.EAZ0001_0001);
    }

    try {
      val decoded = JWT.decode((String) token.getPrincipal());
      val provider =
          new GuavaCachedJwkProvider(
              new UrlJwkProvider(new URL(awsSettings.getCognito().getJwkUrl())));
      val jwk = provider.get(decoded.getKeyId());
      val algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
      val verifier =
          JWT.require(algorithm) //
              .withIssuer(awsSettings.getCognito().getIssuer()) //
              .build();
      val verified = verifier.verify(decoded);
      val username = verified.getClaim("cognito:username").asString();

      return AuthenticatedUser //
          .authenticated(verified.getClaim("cognito:username").asString()) //
          .setAuthorities(authorityMapper.findPermissionByStaffName(username));
    } catch (TokenExpiredException e) {
      log.info("jwt token is expired", e);
      throw new AuthException(ErrorCode.EAZ0001_0002);
    } catch (SignatureVerificationException | InvalidClaimException e) {
      log.info("jwt signature or claim is invalid", e);
      throw new AuthException(ErrorCode.EAZ0001_0003);
    } catch (Exception e) {
      log.error("failed to load user from jwt token", e);
      throw new AuthException(ErrorCode.EAZ0001_0003);
    }
  }
}
