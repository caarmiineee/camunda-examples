package com.example.workflow.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Delegate for order validation
 * Example of Java Delegate pattern - commonly tested in Camunda certification
 */
@Component("orderValidationDelegate")
public class OrderValidationDelegate implements JavaDelegate {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderValidationDelegate.class);
    
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("=== Order Validation Delegate Started ===");
        
        // Get variables from process instance
        String orderId = (String) execution.getVariable("orderId");
        Double orderAmount = (Double) execution.getVariable("orderAmount");
        
        LOGGER.info("Validating Order ID: {}, Amount: {}", orderId, orderAmount);
        
        // Validation logic
        boolean isValid = true;
        String validationMessage = "Order is valid";
        
        if (orderId == null || orderId.isEmpty()) {
            isValid = false;
            validationMessage = "Order ID is missing";
        } else if (orderAmount == null || orderAmount <= 0) {
            isValid = false;
            validationMessage = "Invalid order amount";
        } else if (orderAmount > 100000) {
            isValid = false;
            validationMessage = "Order amount exceeds maximum limit";
        }
        
        // Set variables back to process
        execution.setVariable("orderValid", isValid);
        execution.setVariable("validationMessage", validationMessage);
        
        LOGGER.info("Validation result: {}, Message: {}", isValid, validationMessage);
        LOGGER.info("=== Order Validation Delegate Completed ===");
    }
}
