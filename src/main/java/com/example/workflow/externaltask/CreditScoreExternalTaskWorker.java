package com.example.workflow.externaltask;

import org.camunda.bpm.client.ExternalTaskClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * External Task Worker for Credit Score Check
 * 
 * External Tasks are important for Camunda certification!
 * Key concepts:
 * - Decouples process engine from worker implementation
 * - Workers poll for jobs using "topics"
 * - Enables microservices architecture
 * - Supports different programming languages
 * - Provides better scalability and fault tolerance
 */
@Component
public class CreditScoreExternalTaskWorker implements CommandLineRunner {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CreditScoreExternalTaskWorker.class);
    private final Random random = new Random();
    
    @Override
    public void run(String... args) {
        LOGGER.info("Starting Credit Score External Task Worker...");
        
        // Create External Task Client
        ExternalTaskClient client = ExternalTaskClient.create()
            .baseUrl("http://localhost:8080/engine-rest")
            .asyncResponseTimeout(10000) // Long polling timeout
            .build();
        
        // Subscribe to topic
        client.subscribe("creditScoreCheck")
            .lockDuration(10000) // Lock for 10 seconds
            .handler((externalTask, externalTaskService) -> {
                try {
                    LOGGER.info("=== Processing Credit Score Check ===");
                    
                    // Get variables from process
                    String loanApplicationId = externalTask.getVariable("loanApplicationId");
                    String customerId = externalTask.getVariable("customerId");
                    
                    LOGGER.info("Loan Application ID: {}", loanApplicationId);
                    LOGGER.info("Customer ID: {}", customerId);
                    
                    // Simulate credit score check (external API call in real scenario)
                    Thread.sleep(2000);
                    int creditScore = 600 + random.nextInt(201); // Random score between 600-800
                    
                    LOGGER.info("Credit Score Retrieved: {}", creditScore);
                    
                    // Prepare output variables
                    Map<String, Object> variables = new HashMap<>();
                    variables.put("creditScore", creditScore);
                    variables.put("creditCheckDate", new java.util.Date());
                    variables.put("creditCheckCompleted", true);
                    
                    // Complete the external task
                    externalTaskService.complete(externalTask, variables);
                    
                    LOGGER.info("=== Credit Score Check Completed ===");
                    
                } catch (Exception e) {
                    LOGGER.error("Error processing credit score check", e);
                    
                    // Handle failure - can retry or report BPMN error
                    externalTaskService.handleFailure(externalTask, 
                        "Credit score check failed", 
                        e.getMessage(), 
                        3, // retries
                        10000); // retry timeout
                }
            })
            .open();
        
        LOGGER.info("Credit Score External Task Worker is now active and listening...");
    }
}
