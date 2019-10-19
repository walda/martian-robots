package com.guidesmiths.robots.exception;

public class InvalidRuleException extends RuntimeException {

    private static final String MESSAGE = "No rule found for instruction %s and direction %s";

    public InvalidRuleException(char instruction, char direction) {
        super(String.format(MESSAGE, instruction, direction));
    }
}
