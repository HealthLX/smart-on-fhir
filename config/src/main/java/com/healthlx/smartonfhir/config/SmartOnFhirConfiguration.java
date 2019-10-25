package com.healthlx.smartonfhir.config;

import com.healthlx.smartonfhir.core.SmartOnFhirAccessTokenResponseClient;
import com.healthlx.smartonfhir.core.SmartOnFhirContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpSession;

@Configuration
public class SmartOnFhirConfiguration {

  @Bean
  SmartOnFhirContext smartOnFhirContext(HttpSession session) {
    return new SmartOnFhirContext(session);
  }

  @Bean
  public SmartOnFhirAccessTokenResponseClient smartOnFhirAccessTokenResponseClient(SmartOnFhirContext context) {
    return new SmartOnFhirAccessTokenResponseClient(context);
  }
}
