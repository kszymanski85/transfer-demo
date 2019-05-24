package com.demo.app.controlers.model.out;

import java.util.ArrayList;
import java.util.List;

public class PaymentStatus {
    private final List<PaymentTransferStatus> completed;
    private final List<PaymentTransferStatus> failed;
    private String status;

    public PaymentStatus() {
        completed = new ArrayList<>();
        failed = new ArrayList<>();
        status = "";
    }

    public List<PaymentTransferStatus> getCompleted() {
        return completed;
    }

    public List<PaymentTransferStatus> getFailed() {
        return failed;
    }

    public String  getStatus() {
        return status;
    }


    public void addTransferStatus(String userId, TransferStatus transferStatus) {
        PaymentTransferStatus paymentTransferStatus = new PaymentTransferStatus(transferStatus.getCode(), transferStatus.getMessage(), userId);
        if(transferStatus.hasError()) {
            failed.add(paymentTransferStatus);
        } else {
            completed.add(paymentTransferStatus);
        }
        status = calculateStatus();
    }

    private String calculateStatus() {
        if(completed.size() > 0 && failed.isEmpty()) {
            return "ALL COMPLETED";
        }
        if(failed.size() > 0 && completed.isEmpty()) {
            return "ALL FAILED";
        }
        return "PARTIALLY-COMPLETED";
    }
}
