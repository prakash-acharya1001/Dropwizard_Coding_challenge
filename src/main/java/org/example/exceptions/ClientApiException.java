package org.example.exceptions;

public class ClientApiException  extends RuntimeException{
    public ClientApiException(String message) {
        super(message);
    }
}
