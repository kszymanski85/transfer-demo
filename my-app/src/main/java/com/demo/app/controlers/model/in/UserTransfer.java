package com.demo.app.controlers.model.in;

import java.math.BigInteger;

public class UserTransfer {
    private Long amount;
    private String name;
    private BigInteger account;
    private String address;
    private String description;

    public UserTransfer() {
        // deserialization
    }

    public UserTransfer(Long amount, String name, BigInteger account, String address, String description) {
        this.amount = amount;
        this.name = name;
        this.account = account;
        this.address = address;
        this.description = description;
    }

    public Long getAmount() {
        return amount;
    }

    public String getName() {
        return name;
    }

    public BigInteger getAccount() {
        return account;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

}
