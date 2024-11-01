package org.tix.soa2.exception.customAdvice;

public class ValidationException  extends RuntimeException{
    public ValidationException() {
        super("Validation Failed");
    }
}
