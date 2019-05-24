package com.demo.app.entity.DAO;

import com.demo.app.entity.config.TransferDirection;

import java.math.BigInteger;
import java.time.LocalDateTime;

public class TransferEntity {
    private final Long transferId;
    private final Long amount;
    private final String recipientName;
    private final BigInteger recipientAccount;
    private final String recipientAddress;
    private final String senderName;
    private final BigInteger senderAccount;
    private final String senderAddress;
    private final String description;
    private final TransferDirection direction;
    private final LocalDateTime transferDate;

    public TransferEntity(Long transferId,Long amount, String recipientName, BigInteger recipientAccount,
                          String recipientAddress, String senderName, BigInteger senderAccount, String senderAddress,
                          String description, TransferDirection direction, LocalDateTime transferDate) {
        this.transferId = transferId;
        this.amount = amount;
        this.recipientName = recipientName;
        this.recipientAccount = recipientAccount;
        this.recipientAddress = recipientAddress;
        this.senderName = senderName;
        this.senderAccount = senderAccount;
        this.senderAddress = senderAddress;
        this.description = description;
        this.direction = direction;
        this.transferDate = transferDate;
    }

    public Long getTransferId() {
        return transferId;
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

    public TransferDirection getDirection() {
        return direction;
    }

    public LocalDateTime getTransferDate() {
        return transferDate;
    }
}
