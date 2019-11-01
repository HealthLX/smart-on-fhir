package com.healthlx.smartonfhir.core;

import org.springframework.security.web.savedrequest.DefaultSavedRequest;

import java.util.LinkedHashMap;

/**
 * Wrapper on top of {@link DefaultSavedRequest} to handle SMART on FHIR launch attributes
 */
class SmartOnFhirSavedRequest {

  private final DefaultSavedRequest savedRequest;

  SmartOnFhirSavedRequest(DefaultSavedRequest savedRequest) {
    this.savedRequest = savedRequest;
  }

  LinkedHashMap<String, Object> getSmartLaunchParameters() {
    LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();

    if (savedRequest != null) {
      String launch = getSavedRequestParameter(savedRequest, "launch");
      if (launch != null) {
        parameters.put("launch", launch);
      }

      String iss = getSavedRequestParameter(savedRequest, "iss");
      if (iss != null) {
        parameters.put("aud", iss);
      }
    }

    return parameters;
  }

  private String getSavedRequestParameter(DefaultSavedRequest savedRequest, String parameterName) {
    String[] parameter = savedRequest.getParameterValues(parameterName);
    String result = null;
    if (parameter != null && parameter.length == 1) {
      result = parameter[0];
    }
    return result;
  }
}
