package com.healthlx.smartonfhir.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SmartOnFhirContextTest {

  @DisplayName("Context should be bound to a session")
  @Test
  void contextIsBound() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    SmartOnFhirContext.set(new HashMap<>());
    assertEquals(request.getSession()
        .getAttribute(SmartOnFhirContext.SMART_CONTEXT_SESSION_KEY), SmartOnFhirContext.get());
  }

  @DisplayName("Verify Context parameters")
  @Test
  void contextParameters() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    //Initialize security context
    DefaultOidcUser user = mock(DefaultOidcUser.class);
    when(user.getAttribute(anyString())).thenReturn("test-profile");
    SecurityContextHolder.getContext()
        .setAuthentication(new OAuth2AuthenticationToken(user, new ArrayList<GrantedAuthority>(), "hello"));
    //Initialize attributes
    Map<String, Object> attributes = new HashMap<>();
    attributes.put("patient", "123");
    attributes.put("encounter", "234");
    SmartOnFhirContext.set(attributes);
    //Verify parameters
    assertEquals("123", SmartOnFhirContext.get()
        .getPatient());
    assertEquals("234", SmartOnFhirContext.get()
        .getEncounter());
    assertEquals("test-profile", SmartOnFhirContext.get()
        .getProfile());
  }

}