package org.acme;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import static org.acme.TestHeaderRequestContextFilter.TEST_HEADER_NAME;

@Path("/hello")
public class GreetingResource {

    @Inject
    PaymentService paymentService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        paymentService.processPayment("transaction details");

        return "Hello from Quarkus REST";
    }

    @GET
    @Path("/header")
    @Produces(MediaType.TEXT_PLAIN)
    public String testHeaderValue() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return "missing";
        }
        Object value = attributes.getAttribute(TEST_HEADER_NAME, RequestAttributes.SCOPE_REQUEST);
        return value == null ? "missing" : value.toString();
    }
}
