package com.healthlx.smartonfhir.core;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This is an extension over {@link SavedRequestAwareAuthenticationSuccessHandler} class that creates a {@link
 * SmartOnFhirContext} instance on successful authentication.
 */
public class Oauth2TokenResponseAwareAuthenticationSuccessHandler
    extends SavedRequestAwareAuthenticationSuccessHandler {

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws ServletException, IOException {
    super.onAuthenticationSuccess(request, response, authentication);

    Object tokenResponse = RequestContextUtil.getRequest()
        .getAttribute(SmartOnFhirAccessTokenResponseClient.TOKEN_RESPONSE_ATTRIBUTE_KEY);
    Assert.notNull(tokenResponse, "No OAuth2AccessTokenResponse instance found in request attributes.");
    Assert.isInstanceOf(OAuth2AccessTokenResponse.class, tokenResponse,
        "Token response is not of type OAuth2AccessTokenResponse");

    SmartOnFhirContext.set(authentication, ((OAuth2AccessTokenResponse) tokenResponse).getAdditionalParameters());
  }
}
