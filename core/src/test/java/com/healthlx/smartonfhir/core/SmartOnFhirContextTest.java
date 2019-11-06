package com.healthlx.smartonfhir.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpSession;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class SmartOnFhirContextTest {

  @DisplayName("Patient should be queried if present in session")
  @Test
  void patientQuery() {
    HttpSession mock = Mockito.mock(HttpSession.class);
    when(mock.getAttribute(eq(SmartOnFhirContext.SMART_CONTEXT_SESSION_KEY)))
        .thenReturn(Collections.singletonMap("patient", "Patient/TEST_ID"));
    SmartOnFhirContext smartOnFhirContext = new SmartOnFhirContext(mock, null);

    assertEquals("Patient/TEST_ID", smartOnFhirContext.getPatient());
  }

  @DisplayName("Exception should be thrown if patient is queried before authentication")
  @Test
  void patientShouldBeQueriedAfterAuth() {
    SmartOnFhirContext smartOnFhirContext = new SmartOnFhirContext(Mockito.mock(HttpSession.class),
        null);
    assertThrows(IllegalArgumentException.class, smartOnFhirContext::getPatient);
  }

  @DisplayName("Exception should be thrown if profile is queried before authentication")
  @Test
  void profileShouldBeQueriedAfterAuth() {
    SmartOnFhirContext smartOnFhirContext = new SmartOnFhirContext(Mockito.mock(HttpSession.class),
        null);
    assertThrows(IllegalArgumentException.class, smartOnFhirContext::getProfile);
  }
}