package com.healthlx.smartonfhir.sample;

import com.healthlx.smartonfhir.config.EnableSmartOnFhir;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableSmartOnFhir
@SpringBootApplication
class SampleApplication {

  public static void main(String[] args) {
    SpringApplication.run(SampleApplication.class, args);
  }

}
