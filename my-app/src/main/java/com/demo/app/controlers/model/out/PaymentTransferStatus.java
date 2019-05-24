package com.demo.app.controlers.model.out;

import com.demo.app.controlers.model.config.ValidationCode;

public class PaymentTransferStatus extends TransferStatus {
    private final String recipient;

    public PaymentTransferStatus(ValidationCode code, String message, String recipient) {
        super(code, message);
        this.recipient = recipient;
    }

    public String getRecipient() {
        return recipient;
    }
}
