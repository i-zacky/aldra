package aldra.api.framework.auth;

import aldra.database.domain.entity.user.gen.Staff;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticatedUser extends Staff implements UserDetails {

  boolean accountNonExpired;

  boolean accountNonLocked;

  boolean credentialsNonExpired;

  boolean enabled;

  List<String> authorities;

  private static final List<GrantedAuthority> DEFAULT_AUTHORITIES =
      Collections.singletonList(new SimpleGrantedAuthority("ROLE_NONE"));

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    if (CollectionUtils.isEmpty(authorities)) {
      return DEFAULT_AUTHORITIES;
    }
    return authorities.stream() //
        .map(SimpleGrantedAuthority::new) //
        .collect(Collectors.toList());
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
