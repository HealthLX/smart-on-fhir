package com.healthlx.smartonfhir.core;

import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Utility class with simple methods to deal with {@link RequestContextHolder}.
 */
public final class RequestContextUtil {

  private RequestContextUtil() {}

  /**
   * Get the current request bound to the {@link RequestContextHolder} attributes.
   *
   * @return the current {@link HttpServletRequest} instance
   */
  public static HttpServletRequest getRequest() {
    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
    Assert.notNull(requestAttributes, "No RequestAttributes object is currently bound to the thread.");
    return ((ServletRequestAttributes) requestAttributes).getRequest();
  }
}
