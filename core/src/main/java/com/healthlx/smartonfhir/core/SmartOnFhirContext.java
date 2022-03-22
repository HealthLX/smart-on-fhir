package com.healthlx.smartonfhir.core;

import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.util.Assert;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

/**
 * Class that provides API to get SMART on FHIR launch context attributes. User must be authenticated and authorized
 * before using it.
 */
public final class SmartOnFhirContext {

  //We leave this constant as public in case the RequestAttributes instance is not available. For example while using
  // the WebSockets.
  public static final String SMART_CONTEXT_SESSION_KEY = "SMART_ON_FHIR_CONTEXT";

  private static final String FHIR_USER_CLAIM = "fhirUser";
  private static final String PROFILE_CLAIM = "profile";

  private static final String PATIENT_PARAM = "patient";
  private static final String ENCOUNTER_PARAM = "encounter";
  private static final String FHIR_CONTEXT_PARAM = "fhirContext";
  private static final String NEED_PATIENT_BANNER_PARAM = "need_patient_banner";
  private static final String INTENT_PARAM = "intent";
  private static final String SMART_STYLE_URL_PARAM = "smart_style_url";
  private static final String TENANT_PARAM = "tenant";

  private final String fhirUser;
  private final String patient;
  private final String encounter;
  private final String[] fhirContext;
  private final Boolean needPatientBanner;
  private final String intent;
  private final String smartStyleUrl;
  private final String tenant;

  private SmartOnFhirContext(OidcUser user, Map<String, Object> parameters) {
    this.fhirUser = (String) Optional.ofNullable(user.getClaim(FHIR_USER_CLAIM))
        .orElse(user.getClaim(PROFILE_CLAIM));
    patient = (String) parameters.get(PATIENT_PARAM);
    encounter = (String) parameters.get(ENCOUNTER_PARAM);
    fhirContext = Optional.ofNullable(parameters.get(FHIR_CONTEXT_PARAM))
        .map(String[].class::cast)
        .orElse(new String[0]);
    needPatientBanner = Optional.ofNullable((String) parameters.get(NEED_PATIENT_BANNER_PARAM))
        .map(Boolean::parseBoolean)
        .orElse(null);
    intent = (String) parameters.get(INTENT_PARAM);
    smartStyleUrl = (String) parameters.get(SMART_STYLE_URL_PARAM);
    tenant = (String) parameters.get(TENANT_PARAM);
  }

  public static SmartOnFhirContext get() {
    Object contextObj = getSession().getAttribute(SMART_CONTEXT_SESSION_KEY);
    Assert.state(contextObj != null, "No SmartOnFhirContext object is found within the current HttpSession instance.");
    Assert.state(contextObj instanceof SmartOnFhirContext, "Smart-On-FHIR context type is invalid.");
    return (SmartOnFhirContext) contextObj;
  }

  static void set(Authentication authentication, Map<String, Object> tokenResponseAdditionalParameters) {
    Assert.state(authentication.getPrincipal() instanceof OidcUser, "Current user is not an OidcUser.");
    SmartOnFhirContext context = new SmartOnFhirContext((OidcUser) authentication.getPrincipal(),
        tokenResponseAdditionalParameters);
    getSession().setAttribute(SMART_CONTEXT_SESSION_KEY, context);
  }

  @Nullable
  public String getFhirUser() {
    return fhirUser;
  }

  @Nullable
  public String getPatient() {
    return patient;
  }

  @Nullable
  public String getEncounter() {
    return encounter;
  }

  public String[] getFhirContext() {
    return fhirContext;
  }

  @Nullable
  public Boolean getNeedPatientBanner() {
    return needPatientBanner;
  }

  @Nullable
  public String getIntent() {
    return intent;
  }

  @Nullable
  public String getSmartStyleUrl() {
    return smartStyleUrl;
  }

  @Nullable
  public String getTenant() {
    return tenant;
  }

  private static HttpSession getSession() {
    return RequestContextUtil.getRequest()
        .getSession();
  }

  @Override
  public String toString() {
    return new StringBuilder().append(super.toString())
        .append(": {fhirUser='")
        .append(fhirUser)
        .append("', patient='")
        .append(patient)
        .append("', encounter='")
        .append(encounter)
        .append("', fhirContext=")
        .append(Arrays.toString(fhirContext))
        .append(", need_patient_banner=")
        .append(needPatientBanner)
        .append(", intent='")
        .append(intent)
        .append("', smart_style_url='")
        .append(smartStyleUrl)
        .append("', tenant='")
        .append(tenant)
        .append("'}")
        .toString();
  }
}
