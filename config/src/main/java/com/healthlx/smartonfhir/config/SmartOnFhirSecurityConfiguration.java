package com.healthlx.smartonfhir.config;

import com.healthlx.smartonfhir.core.Oauth2TokenResponseAwareAuthenticationSuccessHandler;
import com.healthlx.smartonfhir.core.SmartOnFhirAccessTokenResponseClient;
import com.healthlx.smartonfhir.core.SmartOnFhirAuthRequestResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
class SmartOnFhirSecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  private SmartOnFhirAuthRequestResolver smartOnFhirAuthRequestResolver;
  @Autowired
  private SmartOnFhirAccessTokenResponseClient smartOnFhirAccessTokenResponseClient;
  @Autowired
  private Oauth2TokenResponseAwareAuthenticationSuccessHandler authenticationSuccessHandler;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .anyRequest()
        .authenticated()
        .and()
        .csrf()
        .disable()
        .oauth2Login()
        .successHandler(authenticationSuccessHandler)
        .authorizationEndpoint()
        .authorizationRequestResolver(this.smartOnFhirAuthRequestResolver)
        .and()
        .tokenEndpoint()
        .accessTokenResponseClient(this.smartOnFhirAccessTokenResponseClient);
  }

}
