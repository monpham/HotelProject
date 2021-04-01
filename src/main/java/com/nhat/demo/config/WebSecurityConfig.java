package com.nhat.demo.config;

import com.nhat.demo.service.serviceIml.AccountDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private AccountDetailsServiceImpl accountDetailsService;

  @Autowired
  private UrlAuthenticationSuccessHandler urlAuthenticationSuccessHandler;

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    return bCryptPasswordEncoder;
  }

//  @Bean
//  public UrlAuthenticationSuccessHandler urlSuccessHandler(){
//    UrlAuthenticationSuccessHandler urlAuthenticationSuccessHandler = new UrlAuthenticationSuccessHandler();
//    return urlAuthenticationSuccessHandler;
//  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(accountDetailsService).passwordEncoder(passwordEncoder());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.csrf().disable();
    http.authorizeRequests().antMatchers("/login", "/logout").permitAll();
    http.authorizeRequests().antMatchers("/").permitAll();
//    http.authorizeRequests().antMatchers("/").access("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')");
    http.authorizeRequests().antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')");
    http.authorizeRequests().antMatchers("/staff/**").access("hasAnyRole('ROLE_STAFF','ROLE_ADMIN')");
    http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");
    http.authorizeRequests().and().formLogin()
            .loginProcessingUrl("/login")
            .loginPage("/login")
//            .defaultSuccessUrl("/admin")
            .successHandler(urlAuthenticationSuccessHandler)
            .failureUrl("/login?error=true")
            .usernameParameter("username")
            .passwordParameter("password")
            .and().logout().logoutUrl("/logout").logoutSuccessUrl("/logoutSuccessful?logout=true");

  }

}
