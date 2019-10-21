package com.healthlx.smartonfhir.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Component
public class SmartOnFhirAccessTokenResponseClient
    implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {

  private DefaultAuthorizationCodeTokenResponseClient defaultClient;

  private HttpSession session;

  @Autowired
  public SmartOnFhirAccessTokenResponseClient(HttpSession session) {
    defaultClient = new DefaultAuthorizationCodeTokenResponseClient();//todo this might be custom for client, right?
    this.session = session;
  }

  @Override
  public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest authorizationGrantRequest) {
    //todo add context with nice API
    OAuth2AccessTokenResponse tokenResponse = defaultClient.getTokenResponse(authorizationGrantRequest);
    session.setAttribute("smart-context", tokenResponse.getAdditionalParameters());//todo not all should be copied
    return tokenResponse;
  }

}
