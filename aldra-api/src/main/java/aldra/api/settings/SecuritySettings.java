package aldra.api.settings;

import aldra.api.framework.auth.CognitoAuthenticationFilter;
import aldra.api.framework.auth.CognitoAuthenticationProvider;
import aldra.api.framework.auth.CognitoAuthenticationSuccessHandler;
import aldra.api.framework.auth.CognitoAuthenticationUserDetailsService;
import aldra.api.framework.auth.JWTAuthorizationFilter;
import aldra.api.framework.auth.JWTAuthorizationUserDetailsService;
import aldra.common.settings.AWSSettings;
import aldra.common.utils.CognitoHelper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecuritySettings extends WebSecurityConfigurerAdapter {

  private final CORSProperties corsProperties;

  private final AWSSettings awsSettings;

  private final CognitoHelper cognitoHelper;

  @Override
  public void configure(WebSecurity web) {
    web.ignoring() //
            .antMatchers("/actuator/health");
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) {
    // for protected endpoint
    val preAuthProvider = new PreAuthenticatedAuthenticationProvider();
    preAuthProvider.setPreAuthenticatedUserDetailsService(new JWTAuthorizationUserDetailsService(awsSettings));
    auth.authenticationProvider(preAuthProvider);
    // for authentication endpoint
    val authenticationProvider = new CognitoAuthenticationProvider(cognitoHelper);
    authenticationProvider.setUserDetailsService(new CognitoAuthenticationUserDetailsService(cognitoHelper));
    auth.authenticationProvider(authenticationProvider);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable() //
            .cors().configurationSource(corsConfigurationSource()) //
            .and() //
            .formLogin().disable() //
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //
            .and() //
            .authorizeRequests() //
            .antMatchers("/api/v1/public/**").permitAll() //
            .antMatchers("/api/v1/protected/**").authenticated() //
            .and() //
            .exceptionHandling().accessDeniedHandler((req, res, auth) -> res.setStatus(HttpServletResponse.SC_FORBIDDEN)) //
            .and() //
            .addFilterAt(cognitoAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class) //
            .addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
  }

  private CorsConfigurationSource corsConfigurationSource() {
    val config = new CorsConfiguration();
    config.setAllowedOrigins(Arrays.asList(corsProperties.getAllowOrigins()));
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(List.of("*"));

    val source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/api/v1/**", config);
    return source;
  }

  @SneakyThrows
  private Filter cognitoAuthenticationFilter() {
    val filter = new CognitoAuthenticationFilter("/api/v1/public/login", "POST");
    filter.setAuthenticationManager(authenticationManager());
    filter.setAuthenticationSuccessHandler(new CognitoAuthenticationSuccessHandler());
    filter.setAuthenticationFailureHandler((req, res, auth) -> res.setStatus(HttpServletResponse.SC_UNAUTHORIZED));
    return filter;
  }

  @SneakyThrows
  private Filter jwtAuthorizationFilter() {
    val filter = new JWTAuthorizationFilter("/api/v1/protected/**");
    filter.setAuthenticationManager(authenticationManager());
    filter.setAuthenticationSuccessHandler((req, res, auth) -> res.setStatus(HttpServletResponse.SC_OK));
    filter.setAuthenticationFailureHandler((req, res, auth) -> res.setStatus(HttpServletResponse.SC_UNAUTHORIZED));
    return filter;
  }
}
