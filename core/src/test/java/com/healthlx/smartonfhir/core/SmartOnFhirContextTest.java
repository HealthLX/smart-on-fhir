package com.healthlx.smartonfhir.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class SmartOnFhirContextTest {

  @DisplayName("Context should be bound to a session")
  @Test
  void contextIsBound() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    Authentication authentication = spy(Authentication.class);
    doReturn(spy(OidcUser.class)).when(authentication)
        .getPrincipal();
    SmartOnFhirContext.set(authentication, new HashMap<>());
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
    when(user.getClaim(anyString())).thenReturn("Practitioner/012");
    Authentication authentication = spy(Authentication.class);
    doReturn(user).when(authentication)
        .getPrincipal();
    //Initialize attributes
    Map<String, Object> attributes = new HashMap<>();
    attributes.put("patient", "123");
    attributes.put("encounter", "234");
    SmartOnFhirContext.set(authentication, attributes);
    //Verify parameters
    assertEquals("123", SmartOnFhirContext.get()
        .getPatient());
    assertEquals("234", SmartOnFhirContext.get()
        .getEncounter());
    assertEquals("Practitioner/012", SmartOnFhirContext.get()
        .getFhirUser());
    System.out.println(SmartOnFhirContext.get().toString());
  }

}