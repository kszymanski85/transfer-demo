package com.demo.app.entity.DAO;

import java.math.BigInteger;

import static java.util.Objects.isNull;

public class UserEntity {
    private final String userId;
    private final String name;
    private final String surname;
    private final String address;
    private final Long phoneNumber;
    private final boolean isCompany;
    private final Long creditorReferenceId;

    private BigInteger accountNumber;
    private Long amount;

    public UserEntity(String userId, String name, String surname, String address, Long phoneNumber, boolean isCompany, Long creditorReferenceId) {
        this.userId = userId;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.isCompany = isCompany;
        this.creditorReferenceId = creditorReferenceId;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getAddress() {
        return address;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public boolean getIsCompany() {
        return isCompany;
    }

    public Long getCreditorReferenceId() {
        return creditorReferenceId;
    }

    public BigInteger getAccountNumber() {
        return accountNumber;
    }

    public Long getAmount() {
        return amount;
    }

    public UserEntity setAccountData(AccountEntity accountEntity) {
        if(!isNull(accountEntity)) {
            this.accountNumber = accountEntity.getAccountNumber();
            this.amount = accountEntity.getAmount();
        }
        return this;
    }
}
