package com.demo.app.controlers.model.in;

import com.demo.app.controlers.model.in.UserTransfer;
import com.demo.app.entity.DAO.UserEntity;

import java.math.BigInteger;

public class Transfer {
    private Long amount;
    private String recipientName;
    private BigInteger recipientAccount;
    private String recipientAddress;
    private String senderName;
    private BigInteger senderAccount;
    private String senderAddress;
    private String description;

    public Transfer() {
        // deserialization
    }

    public Transfer(UserEntity userEntity, UserTransfer userTransfer) {
        this.amount = userTransfer.getAmount();
        this.recipientName = userTransfer.getName();
        this.recipientAccount = userTransfer.getAccount();
        this.recipientAddress = userTransfer.getAddress();
        this.senderName = userEntity.getName() + " " + userEntity.getSurname();
        this.senderAccount = userEntity.getAccountNumber();
        this.senderAddress = userEntity.getAddress();
        this.description = userTransfer.getDescription();
    }

    public Transfer(Long amount, String recipientName, BigInteger recipientAccount, String recipientAddress,
                    String senderName, BigInteger senderAccount, String senderAddress, String description) {
        this.amount = amount;
        this.recipientName = recipientName;
        this.recipientAccount = recipientAccount;
        this.recipientAddress = recipientAddress;
        this.senderName = senderName;
        this.senderAccount = senderAccount;
        this.senderAddress = senderAddress;
        this.description = description;
    }

    public Long getAmount() {
        return amount;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public BigInteger getRecipientAccount() {
        return recipientAccount;
    }

    public String getRecipientAddress() {
        return recipientAddress;
    }

    public String getSenderName() {
        return senderName;
    }

    public BigInteger getSenderAccount() {
        return senderAccount;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public String getDescription() {
        return description;
    }

}
