package com.example.workflow.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Delegate for notifying customer of loan rejection
 */
@Component("rejectionNotificationDelegate")
public class RejectionNotificationDelegate implements JavaDelegate {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RejectionNotificationDelegate.class);
    
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("=== Rejection Notification Delegate Started ===");
        
        String loanApplicationId = (String) execution.getVariable("loanApplicationId");
        String customerEmail = (String) execution.getVariable("customerEmail");
        String rejectionReason = (String) execution.getVariable("rejectionReason");
        
        LOGGER.info("Sending rejection notification for Loan ID: {}", loanApplicationId);
        LOGGER.info("Customer Email: {}, Reason: {}", customerEmail, rejectionReason);
        
        String emailContent = String.format(
            "Dear Customer,\n\nWe regret to inform you that your loan application %s has been declined.\nReason: %s\n\nPlease contact us for more information.",
            loanApplicationId, rejectionReason != null ? rejectionReason : "Did not meet approval criteria"
        );
        
        LOGGER.info("Email content: {}", emailContent);
        
        execution.setVariable("rejectionNotificationSent", true);
        execution.setVariable("notificationSentDate", new java.util.Date());
        
        LOGGER.info("Rejection notification sent successfully");
        LOGGER.info("=== Rejection Notification Delegate Completed ===");
    }
}
