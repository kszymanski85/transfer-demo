package com.demo.app.controlers.model.out;

import java.math.BigInteger;

public class User {
    private final String userId;
    private final String name;
    private final String address;
    private final Long phoneNumber;
    private final Long amount;
    private final BigInteger accountNumber;
    private final boolean isCompany;

    public User(String userId, String name, String address, Long phoneNumber, Long amount, BigInteger accountNumber, boolean isCompany) {
        this.userId = userId;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.amount = amount;
        this.accountNumber = accountNumber;
        this.isCompany = isCompany;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public Long getAmount() {
        return amount;
    }

    public BigInteger getAccountNumber() {
        return accountNumber;
    }

    public boolean getCompany() {
        return isCompany;
    }
}
