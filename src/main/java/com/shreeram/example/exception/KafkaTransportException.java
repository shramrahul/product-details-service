package com.shreeram.example.exception;

public class KafkaTransportException extends RuntimeException {

    public KafkaTransportException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
