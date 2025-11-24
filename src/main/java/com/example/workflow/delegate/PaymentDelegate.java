package com.example.workflow.delegate;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Delegate for processing payment
 * Demonstrates error handling with BpmnError - important for certification
 */
@Component("paymentDelegate")
public class PaymentDelegate implements JavaDelegate {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentDelegate.class);
    private final Random random = new Random();
    
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("=== Payment Processing Delegate Started ===");
        
        String orderId = (String) execution.getVariable("orderId");
        Double orderAmount = (Double) execution.getVariable("orderAmount");
        Double shippingCost = (Double) execution.getVariable("shippingCost");
        
        double totalAmount = (orderAmount != null ? orderAmount : 0.0) + 
                           (shippingCost != null ? shippingCost : 0.0);
        
        LOGGER.info("Processing payment for Order ID: {}, Total Amount: ${}", orderId, totalAmount);
        
        // Simulate payment processing
        Thread.sleep(1000);
        
        // Simulate random payment failure (10% chance) for testing error handling
        if (random.nextInt(10) == 0) {
            LOGGER.error("Payment failed for Order ID: {}", orderId);
            // Throw BPMN Error - will be caught by boundary event
            throw new BpmnError("PAYMENT_ERROR", "Payment processing failed");
        }
        
        // Payment successful
        String transactionId = "TXN-" + System.currentTimeMillis();
        execution.setVariable("transactionId", transactionId);
        execution.setVariable("totalAmount", totalAmount);
        
        LOGGER.info("Payment successful! Transaction ID: {}", transactionId);
        LOGGER.info("=== Payment Processing Delegate Completed ===");
    }
}
