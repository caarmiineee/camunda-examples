package com.example.workflow.service;

import org.springframework.stereotype.Service;

/**
 * Service for checking inventory
 * Used with Expression in Service Task: #{inventoryService.checkAvailability(orderId)}
 * This demonstrates the Expression pattern - commonly tested in certification
 */
@Service("inventoryService")
public class InventoryService {
    
    public boolean checkAvailability(String orderId) {
        // Simulate inventory check
        // In real application, this would query a database
        System.out.println("Checking inventory for order: " + orderId);
        
        // For demo purposes, always return true
        // In production, this would check actual inventory levels
        return true;
    }
    
    public int getStockLevel(String productId) {
        // Simulate stock level check
        return 100; // Mock value
    }
}
