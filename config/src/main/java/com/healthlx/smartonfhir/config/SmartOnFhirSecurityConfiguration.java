package com.healthlx.smartonfhir.config;

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

  private final SmartOnFhirAuthRequestResolver smartOnFhirAuthRequestResolver;

  private final SmartOnFhirAccessTokenResponseClient smartOnFhirAccessTokenResponseClient;

  @Autowired
  SmartOnFhirSecurityConfiguration(SmartOnFhirAuthRequestResolver smartOnFhirAuthRequestResolver,
      SmartOnFhirAccessTokenResponseClient smartOnFhirAccessTokenResponseClient) {
    this.smartOnFhirAuthRequestResolver = smartOnFhirAuthRequestResolver;
    this.smartOnFhirAccessTokenResponseClient = smartOnFhirAccessTokenResponseClient;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .anyRequest()
        .authenticated()
        .and()
        .csrf()
        .disable()
        .oauth2Login()
        .authorizationEndpoint()
        .authorizationRequestResolver(smartOnFhirAuthRequestResolver)
        .and()
        .tokenEndpoint()
        .accessTokenResponseClient(this.smartOnFhirAccessTokenResponseClient);
  }

}
