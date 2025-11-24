package com.example.workflow.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class AssessRiskDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Object scoreObj = execution.getVariable("creditScore");
        String risk = "LOW";
        try {
            if (scoreObj instanceof Number) {
                int score = ((Number) scoreObj).intValue();
                if (score < 600) {
                    risk = "HIGH";
                } else if (score < 700) {
                    risk = "MEDIUM";
                }
            } else if (scoreObj instanceof String) {
                int score = Integer.parseInt((String) scoreObj);
                if (score < 600) {
                    risk = "HIGH";
                } else if (score < 700) {
                    risk = "MEDIUM";
                }
            }
        } catch (Exception e) {
            // ignore and leave default LOW
        }
        execution.setVariable("riskDecision", risk);
    }
}
