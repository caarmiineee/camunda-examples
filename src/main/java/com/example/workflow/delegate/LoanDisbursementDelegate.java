package com.example.workflow.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Delegate for loan disbursement
 */
@Component("loanDisbursementDelegate")
public class LoanDisbursementDelegate implements JavaDelegate {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(LoanDisbursementDelegate.class);
    
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("=== Loan Disbursement Delegate Started ===");
        
        String loanApplicationId = (String) execution.getVariable("loanApplicationId");
        Double loanAmount = (Double) execution.getVariable("loanAmount");
        Double interestRate = (Double) execution.getVariable("interestRate");
        
        LOGGER.info("Disbursing loan: ID={}, Amount=${}, Rate={}%", 
                   loanApplicationId, loanAmount, interestRate);
        
        // Simulate disbursement process
        Thread.sleep(1000);
        
        String disbursementId = "DISB-" + System.currentTimeMillis();
        execution.setVariable("disbursementId", disbursementId);
        execution.setVariable("disbursementDate", new java.util.Date());
        
        LOGGER.info("Loan disbursed successfully! Disbursement ID: {}", disbursementId);
        LOGGER.info("=== Loan Disbursement Delegate Completed ===");
    }
}
