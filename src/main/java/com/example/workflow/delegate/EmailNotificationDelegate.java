package com.example.workflow.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Delegate for sending email notifications
 */
@Component("emailNotificationDelegate")
public class EmailNotificationDelegate implements JavaDelegate {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailNotificationDelegate.class);
    
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("=== Email Notification Delegate Started ===");
        
        String orderId = (String) execution.getVariable("orderId");
        String customerEmail = (String) execution.getVariable("customerEmail");
        String transactionId = (String) execution.getVariable("transactionId");
        
        LOGGER.info("Sending confirmation email to: {}", customerEmail);
        LOGGER.info("Order ID: {}, Transaction ID: {}", orderId, transactionId);
        
        // Simulate email sending
        String emailContent = String.format(
            "Dear Customer,\n\nYour order %s has been confirmed and shipped.\nTransaction ID: %s\n\nThank you for your purchase!",
            orderId, transactionId
        );
        
        LOGGER.info("Email content: {}", emailContent);
        
        execution.setVariable("emailSent", true);
        execution.setVariable("emailSentDate", new java.util.Date());
        
        LOGGER.info("Email sent successfully!");
        LOGGER.info("=== Email Notification Delegate Completed ===");
    }
}
