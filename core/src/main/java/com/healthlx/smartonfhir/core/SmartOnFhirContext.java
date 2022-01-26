package com.healthlx.smartonfhir.core;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.Assert;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Class that provides API to get SMART on FHIR launch context attributes. User must be authenticated and authorized
 * before using it.
 */
public class SmartOnFhirContext {

  //We leave this constant as public in case the RequestAttributes instance is not available. For example while using
  // the WebSockets.
  public static final String SMART_CONTEXT_SESSION_KEY = "SMART_ON_FHIR_CONTEXT";
  private final Map<String, Object> parameters;

  private SmartOnFhirContext(Map<String, Object> parameters) {
    this.parameters = parameters;
  }

  public static SmartOnFhirContext get() {
    Object contextObj = getSession().getAttribute(SMART_CONTEXT_SESSION_KEY);
    Assert.state(contextObj != null, "No SmartOnFhirContext object is found within the current HttpSession instance.");
    Assert.state(contextObj instanceof SmartOnFhirContext, "Smart-On-FHIR context type is invalid.");
    return (SmartOnFhirContext) contextObj;
  }

  static void set(Map<String, Object> parameters) {
    SmartOnFhirContext context = new SmartOnFhirContext(parameters);
    getSession().setAttribute(SMART_CONTEXT_SESSION_KEY, context);
  }

  public String getPatient() {
    return (String) parameters.get("patient");
  }

  public String getEncounter() {
    return (String) parameters.get("encounter");
  }

  public String getProfile() {
    Authentication authentication = SecurityContextHolder.getContext()
        .getAuthentication();
    Assert.notNull(authentication, "Authentication object is missing from security context.");
    return ((OAuth2User) authentication.getPrincipal()).getAttribute("profile");
  }

  private static HttpSession getSession() {
    return RequestContextUtil.getRequest()
        .getSession();
  }
}
