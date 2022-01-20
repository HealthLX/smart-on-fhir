package com.healthlx.smartonfhir.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;

/**
 * Implementation of ResponseClient, stores additional parameters to {@link SmartOnFhirContext}
 */
public class SmartOnFhirAccessTokenResponseClient
    implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {

  private DefaultAuthorizationCodeTokenResponseClient defaultClient;

  @Autowired
  public SmartOnFhirAccessTokenResponseClient() {
    defaultClient = new DefaultAuthorizationCodeTokenResponseClient();
  }

  @Override
  public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest authorizationGrantRequest) {
    OAuth2AccessTokenResponse tokenResponse = defaultClient.getTokenResponse(authorizationGrantRequest);
    SmartOnFhirContext.set(tokenResponse.getAdditionalParameters());
    return tokenResponse;
  }

}
