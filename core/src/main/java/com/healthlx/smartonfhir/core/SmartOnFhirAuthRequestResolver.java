package com.healthlx.smartonfhir.core;

import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Implementation of {@link OAuth2AuthorizationRequestResolver} Resolves request with additional parameters required by
 * SMART on FHIR spec
 */
public class SmartOnFhirAuthRequestResolver implements OAuth2AuthorizationRequestResolver {

  private static final String AUD_PARAM = "aud";
  private static final String LAUNCH_PARAM = "launch";
  private static final String ISS_PARAM = "iss";

  private final OAuth2AuthorizationRequestResolver defaultAuthorizationRequestResolver;
  private String audience;

  public SmartOnFhirAuthRequestResolver(ClientRegistrationRepository clientRegistrationRepository) {

    this.defaultAuthorizationRequestResolver = new DefaultOAuth2AuthorizationRequestResolver(
        clientRegistrationRepository, "/oauth2/authorization");
  }

  /**
   * Set audience in case of a standalone launch, where iss is not passed. It will be overwritten in case of a launch
   * flow.
   *
   * @param audience aud parameter value used during authorize endpoint call.
   */
  public void setAudience(String audience) {
    this.audience = audience;
  }

  @Override
  public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
    OAuth2AuthorizationRequest authorizationRequest = this.defaultAuthorizationRequestResolver.resolve(request);
    return handleSmartOnFhirAuthorizationDetails(authorizationRequest, request);
  }

  @Override
  public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
    OAuth2AuthorizationRequest authorizationRequest = this.defaultAuthorizationRequestResolver.resolve(request,
        clientRegistrationId);
    return handleSmartOnFhirAuthorizationDetails(authorizationRequest, request);

  }

  protected OAuth2AuthorizationRequest handleSmartOnFhirAuthorizationDetails(
      OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request) {
    if (authorizationRequest == null) {
      return null;
    }

    Map<String, Object> additionalParameters = new HashMap<>();
    additionalParameters.putAll(authorizationRequest.getAdditionalParameters());

    // Set audience if defined. This is needed for a standalone launch, where iss is not passed. It will be
    // overwritten in case of a launch flow.
    if (audience != null && !audience.isEmpty()) {
      additionalParameters.put(AUD_PARAM, audience);
    }
    // Try to get parameters from the initial request when a launch flow is used with an arbitrary launch url. There
    // will be no saved request when the application is launched with a /oauth2/authorization/registrationId endpoint.
    DefaultSavedRequest savedRequest = (DefaultSavedRequest) request.getSession()
        .getAttribute("SPRING_SECURITY_SAVED_REQUEST");
    if (savedRequest != null) {
      additionalParameters.putAll(extractSmartParams(p -> getSavedRequestParameter(savedRequest, p)));
    }

    // Try to get parameters from the authorization request query when a launch flow is used with
    // /oauth2/authorization/registrationId url.
    additionalParameters.putAll(extractSmartParams(p -> request.getParameter(p)));

    return OAuth2AuthorizationRequest.from(authorizationRequest)
        .additionalParameters(additionalParameters)
        .build();
  }

  private Map<String, Object> extractSmartParams(Function<String, String> f) {
    Map<String, Object> additionalParameters = new HashMap<>();
    String launch = f.apply(LAUNCH_PARAM);
    if (launch != null) {
      additionalParameters.put(LAUNCH_PARAM, launch);
    }

    String iss = f.apply(ISS_PARAM);
    if (iss != null) {
      additionalParameters.put(AUD_PARAM, iss);
    }
    return additionalParameters;
  }

  private String getSavedRequestParameter(SavedRequest savedRequest, String parameterName) {
    String[] parameter = savedRequest.getParameterValues(parameterName);
    String result = null;
    if (parameter != null && parameter.length > 0) {
      result = parameter[0];
    }
    return result;
  }
}
