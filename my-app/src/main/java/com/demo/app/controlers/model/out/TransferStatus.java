package com.demo.app.controlers.model.out;

import com.demo.app.controlers.model.config.ValidationCode;

import java.beans.Transient;

public class TransferStatus {

    private ValidationCode code;
    private String message;

    public TransferStatus(ValidationCode code, String message) {
        this.code = code;
        this.message = message;
    }

    public ValidationCode getCode() {
        return code;
    }

    public void setCode(ValidationCode code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Transient
    public boolean isValid() {
        return code.equals(ValidationCode.VALID);
    }

    @Transient
    public boolean hasError() {
        return !(code.equals(ValidationCode.VALID) || code.equals(ValidationCode.OK));
    }

}
