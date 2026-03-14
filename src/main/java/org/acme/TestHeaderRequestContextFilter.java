package org.acme;

import jakarta.annotation.Priority;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.ext.Provider;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class TestHeaderRequestContextFilter implements ContainerRequestFilter, ContainerResponseFilter {

    public static final String TEST_HEADER_NAME = "x-test-header";

    @Context
    HttpServletRequest httpServletRequest;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        QuarkusRequestAttributes requestAttributes = new QuarkusRequestAttributes();
        String headerValue = httpServletRequest == null ? null : httpServletRequest.getHeader(TEST_HEADER_NAME);
        if (headerValue != null) {
            requestAttributes.setAttribute(TEST_HEADER_NAME, headerValue, RequestAttributes.SCOPE_REQUEST);
        }
        RequestContextHolder.setRequestAttributes(requestAttributes, true);
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes instanceof QuarkusRequestAttributes quarkusRequestAttributes) {
            quarkusRequestAttributes.requestCompleted();
        }
        RequestContextHolder.resetRequestAttributes();
    }
}
