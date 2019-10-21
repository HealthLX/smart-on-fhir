package com.healthlx.smartonfhir.core;

import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SmartOnFhirAuthRequestResolver implements OAuth2AuthorizationRequestResolver {
    private final OAuth2AuthorizationRequestResolver defaultAuthorizationRequestResolver;

    public SmartOnFhirAuthRequestResolver(
            ClientRegistrationRepository clientRegistrationRepository) {

        this.defaultAuthorizationRequestResolver =
                new DefaultOAuth2AuthorizationRequestResolver(
                        clientRegistrationRepository, "/oauth2/authorization");
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        OAuth2AuthorizationRequest authorizationRequest =
                this.defaultAuthorizationRequestResolver.resolve(request);

        return authorizationRequest != null ?
                customAuthorizationRequest(authorizationRequest,request) :
                null;
    }

    @Override
    public OAuth2AuthorizationRequest resolve(
            HttpServletRequest request, String clientRegistrationId) {

        OAuth2AuthorizationRequest authorizationRequest =
                this.defaultAuthorizationRequestResolver.resolve(
                        request, clientRegistrationId);

        return authorizationRequest != null ?
                customAuthorizationRequest(authorizationRequest, request) :
                null;
    }

    private OAuth2AuthorizationRequest customAuthorizationRequest(
            OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request) {

        Map<String, Object> additionalParameters = new HashMap<>();
        additionalParameters.putAll(authorizationRequest.getAdditionalParameters());
        additionalParameters.putAll(smartAdditionalParameters(request));


        return OAuth2AuthorizationRequest.from(authorizationRequest)
                .additionalParameters(additionalParameters)
                .build();
    }

    private LinkedHashMap<String, Object> smartAdditionalParameters(HttpServletRequest request) {
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();

        //todo refactor to normal holder object and fix this constructor (+add tests)
        DefaultSavedRequest savedRequest = (DefaultSavedRequest) request.getSession().getAttribute(
                "SPRING_SECURITY_SAVED_REQUEST");

        if (savedRequest != null) {
            String launch = getSavedRequestParameter(savedRequest, "launch");
            if (launch != null) {
                parameters.put("launch", launch);
            }

            String iss = getSavedRequestParameter(savedRequest, "iss");
            if (launch != null) {
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
