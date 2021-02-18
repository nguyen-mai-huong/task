package com.ufinity.task;

import com.ufinity.task.security.UserAuthenticationHelperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    return bCryptPasswordEncoder;
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
    configuration.setAllowCredentials(true);
    configuration.setAllowedMethods(Arrays.asList("GET","POST"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  @Autowired
  private UserAuthenticationHelperService helperService;

  @Autowired
  private AuthenticationSuccessHandler successHandler;

  @Autowired
  private CorsConfigurationSource corsConfigurationSource;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors().configurationSource(corsConfigurationSource);

    http.authorizeRequests()
            .antMatchers("/").permitAll()
            .antMatchers("/user/login").permitAll()
            .antMatchers("/singpass/login").permitAll()
            .antMatchers("/login/oauth2/code/singpass").permitAll()
            .antMatchers("/user/logout").permitAll()
            .antMatchers("/user/signup").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin().loginProcessingUrl("/user/login").usernameParameter("username").passwordParameter("password").successHandler(successHandler)
            .and()
            .logout().logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"));

    http.csrf().ignoringAntMatchers("/user/login").ignoringAntMatchers("/user/signup").ignoringAntMatchers("/login/oauth2/code/singpass");

    http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            .maximumSessions(1);
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(helperService).passwordEncoder(passwordEncoder);
  }
}
