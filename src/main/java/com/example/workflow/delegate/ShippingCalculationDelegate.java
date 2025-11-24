package com.example.workflow.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Delegate for calculating shipping costs
 */
@Component("shippingCalculationDelegate")
public class ShippingCalculationDelegate implements JavaDelegate {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ShippingCalculationDelegate.class);
    
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("=== Shipping Calculation Delegate Started ===");
        
        Double orderAmount = (Double) execution.getVariable("orderAmount");
        String shippingCountry = (String) execution.getVariable("shippingCountry");
        
        LOGGER.info("Calculating shipping for amount: {}, country: {}", orderAmount, shippingCountry);
        
        // Calculate shipping cost based on rules
        double shippingCost = 0.0;
        
        if (orderAmount != null && orderAmount > 50) {
            shippingCost = 0.0; // Free shipping over $50
        } else if ("USA".equalsIgnoreCase(shippingCountry)) {
            shippingCost = 5.99;
        } else {
            shippingCost = 15.99;
        }
        
        execution.setVariable("shippingCost", shippingCost);
        
        LOGGER.info("Shipping cost calculated: ${}", shippingCost);
        LOGGER.info("=== Shipping Calculation Delegate Completed ===");
    }
}
