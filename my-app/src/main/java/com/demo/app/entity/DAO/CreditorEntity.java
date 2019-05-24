package com.demo.app.entity.DAO;

import java.math.BigInteger;

public class CreditorEntity {
    private final String name;
    private final String address;
    private final Long companyReference;
    private final BigInteger accountNumber;

    private Long amount;
    private boolean active;

    public CreditorEntity(Long companyReference, String name, String address, BigInteger accountNumber, Long amount) {
        this.companyReference = companyReference;
        this.name = name;
        this.address = address;
        this.accountNumber = accountNumber;
        this.amount = amount;
        active = true;
    }

    public Long getCompanyReference() {
        return companyReference;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return this.address;
    }

    public BigInteger getAccountNumber() {
        return accountNumber;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public boolean isActive() {
        return active;
    }

    public void activate() {
        active = true;
    }

    public void deactivate() {
        active = false;
    }
}
