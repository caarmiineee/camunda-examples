package com.example.workflow.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Delegate for sending reminders (used in loan approval process)
 */
@Component("reminderDelegate")
public class ReminderDelegate implements JavaDelegate {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ReminderDelegate.class);
    
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("=== Reminder Delegate Started ===");
        
        String loanApplicationId = (String) execution.getVariable("loanApplicationId");
        LOGGER.info("Sending reminder for Loan Application ID: {}", loanApplicationId);
        
        // Track number of reminders sent
        Integer reminderCount = (Integer) execution.getVariable("reminderCount");
        if (reminderCount == null) {
            reminderCount = 0;
        }
        reminderCount++;
        
        execution.setVariable("reminderCount", reminderCount);
        execution.setVariable("lastReminderDate", new java.util.Date());
        
        LOGGER.info("Reminder #{} sent successfully", reminderCount);
        LOGGER.info("=== Reminder Delegate Completed ===");
    }
}
