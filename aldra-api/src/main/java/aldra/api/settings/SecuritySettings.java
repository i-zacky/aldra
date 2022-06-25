package aldra.api.settings;

import static aldra.api.ServerConfiguration.VERSION;

import aldra.api.framework.auth.AuthFailureHandler;
import aldra.api.framework.auth.CognitoAuthenticationFilter;
import aldra.api.framework.auth.CognitoAuthenticationProvider;
import aldra.api.framework.auth.CognitoAuthenticationSuccessHandler;
import aldra.api.framework.auth.CognitoAuthenticationUserDetailsService;
import aldra.api.framework.auth.JWTAuthorizationFilter;
import aldra.api.framework.auth.JWTAuthorizationUserDetailsService;
import aldra.api.framework.interceptor.WebAPIAccessLogFilter;
import aldra.common.settings.AWSSettings;
import aldra.common.utils.CognitoHelper;
import aldra.database.domain.repository.user.AuthorityMapper;
import java.util.Arrays;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity( //
    prePostEnabled = true, //
    securedEnabled = true //
    )
public class SecuritySettings {

  private final CORSProperties corsProperties;

  private final AWSSettings awsSettings;

  private final CognitoHelper cognitoHelper;

  private final AuthorityMapper authorityMapper;

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return web ->
        web.ignoring() //
            .antMatchers("/actuator/health");
  }


  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // for protected endpoint
    val preAuthProvider = new PreAuthenticatedAuthenticationProvider();
    preAuthProvider.setPreAuthenticatedUserDetailsService(
        new JWTAuthorizationUserDetailsService(awsSettings, authorityMapper));
    // for authentication endpoint
    val authenticationProvider = new CognitoAuthenticationProvider(cognitoHelper);
    authenticationProvider.setUserDetailsService(
        new CognitoAuthenticationUserDetailsService(cognitoHelper));

    http.csrf()
        .disable() //
        .cors()
        .configurationSource(corsConfigurationSource()) //
        .and() //
        .formLogin()
        .disable() //
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //
        .and() //
        .authorizeRequests() //
        .antMatchers(VERSION + "/public/**")
        .permitAll() //
        .antMatchers(VERSION + "/protected/**")
        .authenticated() //
        .and() //
        .exceptionHandling() //
        .authenticationEntryPoint(
            (req, res, ex) -> res.setStatus(HttpServletResponse.SC_UNAUTHORIZED)) //
        .and() //
        .addFilterAt(cognitoAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class) //
        .addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class) //
        .addFilterBefore(
            new WebAPIAccessLogFilter(), UsernamePasswordAuthenticationFilter.class) //
        .authenticationProvider(preAuthProvider) //
        .authenticationProvider(authenticationProvider);
    return http.build();
  }

  private CorsConfigurationSource corsConfigurationSource() {
    val config = new CorsConfiguration();
    config.setAllowedOrigins(Arrays.asList(corsProperties.getAllowOrigins()));
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(List.of("*"));

    val source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration(VERSION + "/**", config);
    return source;
  }

  @SneakyThrows
  private Filter cognitoAuthenticationFilter() {
    val filter = new CognitoAuthenticationFilter(VERSION + "/public/login", "POST");
    filter.setAuthenticationSuccessHandler(new CognitoAuthenticationSuccessHandler());
    filter.setAuthenticationFailureHandler(new AuthFailureHandler());
    return filter;
  }

  @SneakyThrows
  private Filter jwtAuthorizationFilter() {
    val filter = new JWTAuthorizationFilter(VERSION + "/protected/**");
    filter.setAuthenticationSuccessHandler(
        (req, res, auth) -> res.setStatus(HttpServletResponse.SC_OK));
    filter.setAuthenticationFailureHandler(new AuthFailureHandler());
    return filter;
  }
}
