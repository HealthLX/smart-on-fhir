package com.healthlx.smartonfhir.core;

import org.junit.jupiter.api.Test;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;

import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SmartOnFhirSavedRequestTest {

  @Test
  void getSmartLaunchParameters() {
    DefaultSavedRequest savedRequest = new DefaultSavedRequest.Builder().setParameters(new HashMap<String, String[]>() {{
      put("launch", new String[] {"SOME_CODE_1"});
      put("iss", new String[] {"test:/url"});
    }})
        .build();

    SmartOnFhirSavedRequest smartOnFhirSavedRequest = new SmartOnFhirSavedRequest(savedRequest);

    LinkedHashMap<String, Object> launchParameters = smartOnFhirSavedRequest.getSmartLaunchParameters();
    assertEquals("SOME_CODE_1", launchParameters.get("launch"));
    assertEquals("test:/url", launchParameters.get("aud"));//it should be renamed
  }

}