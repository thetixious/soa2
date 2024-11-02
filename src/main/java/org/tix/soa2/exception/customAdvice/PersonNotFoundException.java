package org.tix.soa2.exception.customAdvice;

public class PersonNotFoundException extends RuntimeException{
    public PersonNotFoundException() {
        super("Person not found");
    }
}
