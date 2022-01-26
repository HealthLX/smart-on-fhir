package com.healthlx.smartonfhir.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;

/**
 * This is an extension over {@link OAuth2AccessTokenResponseClient} class that puts a token response into the current
 * request attributes.This will make accessible by the {@link Oauth2TokenResponseAwareAuthenticationSuccessHandler}
 * instance upon successful authentication.
 */
public class SmartOnFhirAccessTokenResponseClient
    implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {

  public static final String TOKEN_RESPONSE_ATTRIBUTE_KEY = "com.healthlx.smartonfhir.core.TOKEN_RESPONSE";
  private DefaultAuthorizationCodeTokenResponseClient defaultClient;

  @Autowired
  public SmartOnFhirAccessTokenResponseClient() {
    defaultClient = new DefaultAuthorizationCodeTokenResponseClient();
  }

  @Override
  public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest authorizationGrantRequest) {
    OAuth2AccessTokenResponse tokenResponse = defaultClient.getTokenResponse(authorizationGrantRequest);
    //We need to store the token response as it is lost in the OAuth2LoginAuthenticationFilter. Session attributes
    // will not work if the http.sessionFixation().newSession() is used. That is why the request scope is used.
    RequestContextUtil.getRequest()
        .setAttribute(TOKEN_RESPONSE_ATTRIBUTE_KEY, tokenResponse);
    return tokenResponse;
  }

}
