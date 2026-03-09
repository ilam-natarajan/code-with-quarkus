package org.acme;

import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

@Startup
@ApplicationScoped
public class PaymentService {

  public PaymentService() {
    System.out.println("PaymentService created");
    // breakpoint here
  }

  @PostConstruct
  void init() {
    // breakpoint here
    System.out.println("PaymentService initialized");
    var result = 1 + 1; // breakpoint here
    System.out.println(result);
  }

  public String processPayment(String paymentDetails) {
    // breakpoint here
    System.out.println("Processing payment: " + paymentDetails);
    return "Payment processed successfully";
  }
}