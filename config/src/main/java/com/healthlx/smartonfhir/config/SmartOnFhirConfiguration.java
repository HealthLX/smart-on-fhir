package com.healthlx.smartonfhir.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class SmartOnFhirConfiguration {

  @Bean
  public SmartOnFhirAccessTokenResponseClient smartOnFhirAccessTokenResponseClient(SmartOnFhirContext context){
    return new SmartOnFhirAccessTokenResponseClient(context);
  }

  @Bean
  SmartOnFhirContext smartOnFhirContext(HttpSession session){
    return new SmartOnFhirContext(session);
  }
}
