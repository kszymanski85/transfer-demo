package com.demo.app.validators;

import com.demo.app.controlers.model.in.Transfer;
import com.demo.app.controlers.model.in.UserTransfer;
import com.demo.app.entity.DAO.AccountEntity;
import com.demo.app.entity.DAO.CreditorEntity;
import com.demo.app.entity.DAO.UserEntity;
import com.demo.app.controlers.model.config.ValidationCode;
import com.demo.app.controlers.model.out.TransferStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class ValidatorImpl implements Validator {

    private static final Logger LOG = LoggerFactory.getLogger(ValidatorImpl.class);

    private UserEntity user;
    private Transfer transfer;
    private UserTransfer userTransfer;
    private AccountEntity accountEntity;
    private List<CreditorEntity> creditors;

    private List<String> validationOrder;

    private TransferStatus status;

    public ValidatorImpl init() {
        validationOrder = new ArrayList<>();
        status = new TransferStatus(ValidationCode.VALID, "");
        return this;
    }

    public ValidatorImpl validate(UserEntity user) {
        validationOrder.add("USER");
        this.user = user;
        return this;
    }

    public ValidatorImpl validate(UserTransfer userTransfer) {
        validationOrder.add("USER-TRANSFER");
        this.userTransfer = userTransfer;
        return this;
    }

    public ValidatorImpl validate(AccountEntity accountEntity) {
        validationOrder.add("ACCOUNT-ENTITY");
        this.accountEntity = accountEntity;
        return this;
    }

    public ValidatorImpl validate(Transfer transfer) {
        validationOrder.add("TRANSFER");
        this.transfer = transfer;
        return this;
    }

    public ValidatorImpl validate(List<CreditorEntity> creditors) {
        validationOrder.add("CREDITORS");
        this.creditors = creditors;
        return this;
    }

    public TransferStatus run() {
        for(String validator: validationOrder) {
            if(status.hasError()) {
                return status;
            }
            switch(validator) {
                case "USER":
                    status.setMessage(validateUser());
                    continue;
                case "USER-TRANSFER":
                    status.setMessage(validateUserTransfer());
                    continue;
                case "ACCOUNT-ENTITY":
                    status.setMessage(validateAccountEntity());
                    continue;
                case "TRANSFER":
                    status.setMessage(validateTransfer());
                    continue;
                case "CREDITORS":
                    status.setMessage(validateCreditors());
            }
        }
        return status;
    }

    private String validateUser() {
        LOG.info("Validate User");
        ValidationCode code = ValidationCode.USER_ERROR;
        if(isNull(user)) {
            return logAndSet(code, "User doesn't exist");
        }
        if(user.getAmount().equals(0L)) {
            return logAndSet(code, "Account empty");
        }
        return "";
    }

    private String validateUserTransfer() {
        LOG.info("Validate User's transfer");
        ValidationCode code = ValidationCode.USER_TRANSFER_ERROR;
        if(isNull(userTransfer)) {
            return logAndSet(code, "User transfer doesn't exist");
        }
        if(!validationOrder.contains("ACCOUNT-ENTITY")) {
            return logAndSet(code, "Account is required for transfer validation");
        }
        if(userTransfer.getAmount() > accountEntity.getAmount()) {
            return logAndSet(code, "You don't have enough resources to sent the transfer");
        }
        if(userTransfer.getAccount().toString().length() != 16) {
            return logAndSet(code, "Invalid recipient account number length");
        }
        if(userTransfer.getAddress().length() <= 5) {
            return logAndSet(code, "Recipient address should contain almost 5 characters");
        }
        if(userTransfer.getDescription().length() <= 5) {
            return logAndSet(code, "Description should contain almost 5 characters");
        }
        return "";
    }

    private String validateAccountEntity() {
        LOG.info("Validate Account");
        ValidationCode code = ValidationCode.ACCOUNT_ERROR;
        if(isNull(accountEntity)) {
            return logAndSet(code, "Account doesn't exist");
        }
        if(accountEntity.getLocked()) {
            return logAndSet(code, "Account is closed");
        }
        return "";
    }

    private String validateTransfer() {
        LOG.info("Validate Transfer");
        ValidationCode code = ValidationCode.TRANSFER_ERROR;
        if(isNull(transfer)) {
            return logAndSet(code, "Transfer doesn't exist");
        }
        if(transfer.getSenderName().length() <= 5) {
            return logAndSet(code, "Sender name need to has almost 5 character");
        }
        if(transfer.getDescription().length() <= 5) {
            return logAndSet(code, "Description need to has almost 5 character");
        }
        if(transfer.getRecipientAccount().toString().length() != 16) {
            return logAndSet(code, "Destination account length is not match");
        }
        return "";
    }

    private String validateCreditors() {
        LOG.info("Validate Creditors");
        ValidationCode code = ValidationCode.PAYMENT_TRANSFER_ERROR;
        if(!validationOrder.contains("USER")) {
            return logAndSet(code, "User is required for transfer validation");
        }
        if(!user.getIsCompany()) {
            return logAndSet(code, "User is not company");
        }
        if(creditors.isEmpty()) {
            return logAndSet(code, "No assigned creditors");
        }
        if(checkActiveCreditors()) {
            return logAndSet(code, "No active creditors");
        }
        if(checkRequiredAmount()) {
            return logAndSet(code, "Amount is not enough to complete all transfers");
        }
        return "";
    }

    private String logAndSet(ValidationCode code, String message) {
        status.setCode(code);
        LOG.error(message);
        return message;
    }

    private boolean checkActiveCreditors() {
        return creditors.stream()
                .filter(CreditorEntity::isActive)
                .collect(Collectors.toList())
                .isEmpty();
    }

    private boolean checkRequiredAmount() {
        return user.getAmount() < creditors.stream().mapToLong(CreditorEntity::getAmount).sum();
    }

}
