package com.healthlx.smartonfhir.config;

import com.healthlx.smartonfhir.core.Oauth2TokenResponseAwareAuthenticationSuccessHandler;
import com.healthlx.smartonfhir.core.SmartOnFhirAccessTokenResponseClient;
import com.healthlx.smartonfhir.core.SmartOnFhirAuthRequestResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

@Configuration
class SmartOnFhirConfiguration {

  @Bean
  SmartOnFhirAccessTokenResponseClient smartOnFhirAccessTokenResponseClient() {
    return new SmartOnFhirAccessTokenResponseClient();
  }

  @Bean
  SmartOnFhirAuthRequestResolver smartOnFhirAuthRequestResolver(
      ClientRegistrationRepository clientRegistrationRepository) {
    return new SmartOnFhirAuthRequestResolver(clientRegistrationRepository);
  }

  @Bean
  Oauth2TokenResponseAwareAuthenticationSuccessHandler authenticationSuccessHandler() {
    return new Oauth2TokenResponseAwareAuthenticationSuccessHandler();
  }
}
