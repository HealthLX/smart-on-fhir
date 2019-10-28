package com.healthlx.smartonfhir.config;

import com.healthlx.smartonfhir.core.SmartOnFhirAccessTokenResponseClient;
import com.healthlx.smartonfhir.core.SmartOnFhirAuthRequestResolver;
import com.healthlx.smartonfhir.core.SmartOnFhirContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

import javax.servlet.http.HttpSession;

@Configuration
class SmartOnFhirConfiguration {

  @Bean
  SmartOnFhirContext smartOnFhirContext(HttpSession session) {
    return new SmartOnFhirContext(session);
  }

  @Bean
  SmartOnFhirAccessTokenResponseClient smartOnFhirAccessTokenResponseClient(SmartOnFhirContext context) {
    return new SmartOnFhirAccessTokenResponseClient(context);
  }

  @Bean
  SmartOnFhirAuthRequestResolver smartOnFhirAuthRequestResolver(ClientRegistrationRepository clientRegistrationRepository){
    return new SmartOnFhirAuthRequestResolver(clientRegistrationRepository);
  }
}
