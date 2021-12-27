package aldra.api.framework.auth;

import aldra.database.domain.entity.user.gen.Staff;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.val;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class AuthenticatedUser extends Staff implements UserDetails {

  private boolean accountNonExpired;

  private boolean accountNonLocked;

  private boolean credentialsNonExpired;

  private boolean enabled;

  private static final List<GrantedAuthority> AUTHORITIES = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    // TODO fixed value
    return AUTHORITIES;
  }

  @Override
  public String getUsername() {
    // username of Spring Security is staff.name
    return super.getName();
  }

  @Override
  public String getPassword() {
    // password save on AWS Cognito
    return null;
  }

  public static AuthenticatedUser authenticated(String username) {
    val user = new AuthenticatedUser();
    user.setName(username);
    user.setAccountNonExpired(true);
    user.setAccountNonLocked(true);
    user.setCredentialsNonExpired(true);
    user.setEnabled(true);
    return user;
  }
}
