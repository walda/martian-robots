package com.guidesmiths.robots.exception;

public class UnparseableTokenException extends RuntimeException {

    private final static String MESSAGE = "Input '%s' cannot be parsed";

    public UnparseableTokenException(String message) {
        super(String.format(MESSAGE, message));
    }
}
