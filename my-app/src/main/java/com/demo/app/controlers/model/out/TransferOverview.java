package com.demo.app.controlers.model.out;

import com.demo.app.controlers.model.in.UserTransfer;

import java.math.BigInteger;

public class TransferOverview extends UserTransfer {
    private final String transferDate;

    public TransferOverview(Long amount, String name, BigInteger account, String address, String description, String transferDate) {
        super(amount, name, account, address, description);
        this.transferDate = transferDate;
    }

    public String  getTransferDate() {
        return transferDate;
    }
}
