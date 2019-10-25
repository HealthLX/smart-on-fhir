package com.healthlx.smartonfhir.config;

import com.healthlx.smartonfhir.core.SmartOnFhirAccessTokenResponseClient;
import com.healthlx.smartonfhir.core.SmartOnFhirAuthRequestResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

@Configuration
@EnableWebSecurity
public class SmartOnFhirSecurityConfiguration extends WebSecurityConfigurerAdapter {

  private ClientRegistrationRepository clientRegistrationRepository;

  private SmartOnFhirAccessTokenResponseClient smartOnFhirAccessTokenResponseClient;

  @Autowired
  public SmartOnFhirSecurityConfiguration(ClientRegistrationRepository clientRegistrationRepository,
      SmartOnFhirAccessTokenResponseClient smartOnFhirAccessTokenResponseClient) {
    this.clientRegistrationRepository = clientRegistrationRepository;
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
        .authorizationRequestResolver(new SmartOnFhirAuthRequestResolver(this.clientRegistrationRepository))
        .and()
        .tokenEndpoint()
        .accessTokenResponseClient(this.smartOnFhirAccessTokenResponseClient);
  }

}
