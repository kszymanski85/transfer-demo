package com.demo.app.entity.DAO;

import java.math.BigInteger;

public class AccountEntity {

    private final BigInteger accountNumber;

    private String ownerId;
    private Long amount;
    private boolean locked;

    public AccountEntity(String ownerId, BigInteger accountNumber, Long amount, boolean locked) {
        this.ownerId = ownerId;
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.locked = locked;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public BigInteger getAccountNumber() {
        return accountNumber;
    }

    public Long getAmount() {
        return amount;
    }

    public boolean getLocked() {
        return locked;
    }

    public void lock() {
        locked = true;
    }

    public void unlock() {
        locked = false;
    }

    public void increase(Long value) {
        amount += value;
    }

    public void decrease(Long value) {
        amount -= value;
    }

}
