package com.unisul.product_storage.exceptions.handler;

import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final HttpStatus status;
    private final String businessMessage;

    public BusinessException(String detailedMessage) {
        this(HttpStatus.BAD_REQUEST, "Business exception", detailedMessage);
    }

    public BusinessException(HttpStatus status, String detailedMessage) {
        this(status, "Business exception", detailedMessage);
    }

    public BusinessException(HttpStatus status, String businessMessage, String detailedMessage) {
        super(detailedMessage);
        this.status = status;
        this.businessMessage = businessMessage;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getBusinessMessage() {
        return businessMessage;
    }
}