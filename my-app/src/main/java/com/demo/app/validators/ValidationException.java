package com.demo.app.validators;

import com.demo.app.controlers.model.out.TransferStatus;

public class ValidationException extends Exception {
    private final TransferStatus status;

    public ValidationException(TransferStatus status) {
        super();
        this.status = status;
    }

    public TransferStatus getStatus() {
        return status;
    }
}
