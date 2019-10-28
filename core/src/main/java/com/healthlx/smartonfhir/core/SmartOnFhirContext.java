package com.healthlx.smartonfhir.core;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.Assert;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Class that provides API to get SMART on FHIR launch context attributes. User must be authenticated an authorized
 * before using it.
 */
public class SmartOnFhirContext {

  static final String SMART_CONTEXT_SESSION_KEY = "SMART_ON_FHIR_CONTEXT";

  private HttpSession session;

  public SmartOnFhirContext(HttpSession session) {
    this.session = session;
  }

  public String getPatient() {
    return getContextValue("patient");
  }

  public String getEncounter() {
    return getContextValue("encounter");
  }

  public String getProfile() {
    Authentication authentication = SecurityContextHolder.getContext()
        .getAuthentication();
    Assert.notNull(authentication, "Authentication object is missing from security context.");
    return ((OAuth2User) authentication.getPrincipal()).getAttribute("profile");
  }

  void setContextData(Map<String, Object> additionalParameters) {
    session.setAttribute(SMART_CONTEXT_SESSION_KEY, additionalParameters);
  }

  public String getContextValue(String contextKey) {
    Map attribute = (Map) session.getAttribute(SMART_CONTEXT_SESSION_KEY);
    Assert.notNull(attribute, "SMART on FHIR context is not initialized. Check authorization.");
    return (String) attribute.get(contextKey);
  }
}
