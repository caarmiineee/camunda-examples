package com.example.workflow.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Delegate for reserving items in inventory
 */
@Component("reserveItemsDelegate")
public class ReserveItemsDelegate implements JavaDelegate {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ReserveItemsDelegate.class);
    
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("=== Reserve Items Delegate Started ===");
        
        String orderId = (String) execution.getVariable("orderId");
        LOGGER.info("Reserving items for Order ID: {}", orderId);
        
        // Simulate reservation process
        Thread.sleep(1000); // Simula elaborazione
        
        String reservationId = "RES-" + System.currentTimeMillis();
        execution.setVariable("reservationId", reservationId);
        
        LOGGER.info("Items reserved with Reservation ID: {}", reservationId);
        LOGGER.info("=== Reserve Items Delegate Completed ===");
    }
}
