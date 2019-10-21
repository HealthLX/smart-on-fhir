package com.healthlx.smartonfhir.core;

import org.junit.jupiter.api.Test;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;

import java.util.Collections;
import java.util.stream.Stream;

class CustomAuthorizationRequestResolverTest {

    @Test
    void lambdaExpressions() {
        DefaultSavedRequest savedRequest = new DefaultSavedRequest.Builder()
                .setParameters(Collections.singletonMap("launch", new String[]{"SOME_CODE_1"}))
                .build();
        //bla bla bla consider split logic from spring flow integration
    }
}