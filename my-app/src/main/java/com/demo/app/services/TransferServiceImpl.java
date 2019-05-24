package com.demo.app.services;

import com.demo.app.controlers.model.in.Transfer;
import com.demo.app.controlers.model.in.UserTransfer;
import com.demo.app.controlers.model.out.PaymentStatus;
import com.demo.app.controlers.model.out.TransferOverview;
import com.demo.app.entity.DAO.AccountEntity;
import com.demo.app.entity.DAO.CreditorEntity;
import com.demo.app.entity.DAO.TransferEntity;
import com.demo.app.entity.DAO.UserEntity;
import com.demo.app.entity.DataEntity;
import com.demo.app.entity.config.TransferDirection;
import com.demo.app.validators.ValidationException;
import com.demo.app.validators.Validator;
import com.demo.app.controlers.model.config.ValidationCode;
import com.demo.app.controlers.model.out.TransferStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class TransferServiceImpl implements TransferService {
    private static final Logger LOG = LoggerFactory.getLogger(TransferServiceImpl.class);

    private Validator validator;

    public TransferServiceImpl(Validator validator) {
        this.validator = validator;
    }

    public List<TransferOverview> getOutgoingTransfers(String userId) {
        return DataEntity.execute().getOutgoingTransfers(userId).stream()
                .map(tsr -> new TransferOverview(tsr.getAmount(), tsr.getRecipientName(), tsr.getRecipientAccount(),
                        tsr.getRecipientAddress(), tsr.getDescription(), tsr.getTransferDate().toString()))
                .collect(Collectors.toList());
    }

    public List<TransferOverview> getIncomingTransfers(String userId) {
        return DataEntity.execute().getIncomingTransfers(userId).stream()
                .map(tsr -> new TransferOverview(tsr.getAmount(), tsr.getSenderName(), tsr.getSenderAccount(),
                        tsr.getSenderAddress(), tsr.getDescription(), tsr.getTransferDate().toString()))
                .collect(Collectors.toList());
    }

    public TransferEntity getTransferDetails(Long transferId) {
        return DataEntity.execute().getTransferDetails(transferId);
    }

    public TransferStatus receiveTransfer(Transfer transfer) {
        TransferStatus status = validator.init().validate(transfer).run();
        if(status.isValid()) {
            saveIncomingTransfer(transfer, status);
        }
        return status;
    }

    public TransferStatus sendTransfer(String userId, UserTransfer userTransfer) {
        UserEntity user = DataEntity.execute().getUser(userId);
        AccountEntity userAccount = DataEntity.execute().getAccountByUserId(userId);
        TransferStatus status = validator.init().validate(user).validate(userTransfer).validate(userAccount).run();
        if(status.isValid()) {
            Transfer transfer = new Transfer(user, userTransfer);
            AccountEntity destinationAccount = DataEntity.execute().getAccountByNumber(userTransfer.getAccount());
            if(isNull(destinationAccount)) {
                sendExternal(transfer, status);
            } else {
                saveInternalTransfer(transfer, status);
            }
        }
        return status;
    }

    public PaymentStatus sendPaymentTransfer(String userId) throws ValidationException {
        PaymentStatus paymentStatus = new PaymentStatus();
        UserEntity user = DataEntity.execute().getUser(userId);
        List<CreditorEntity> creditors = DataEntity.execute().getCreditors(userId);
        TransferStatus validation = validator.init().validate(user).validate(creditors).run();
        if(validation.isValid()) {
            for(CreditorEntity crd : creditors) {
                UserTransfer userTransfer = new UserTransfer(crd.getAmount(), crd.getName(), crd.getAccountNumber(), crd.getAddress(), "Payment from " + userId);
                paymentStatus.addTransferStatus(crd.getName(), sendTransfer(userId, userTransfer));
            }
        } else {
            throw new ValidationException(validation);
        }
        return paymentStatus;
    }

    private void sendExternal(Transfer transfer, TransferStatus status) {
        LOG.info("Sending to External provider");
        // make some operation/transformations - if necessary - and sent transfer outside
        // to queue or storing systems who manage success/failed outgoing transactions
        // actions depend further transfer process components
        if(transfer.getRecipientName().equals("FaiExternal")) {
            LOG.error("Cannot deliver to external account: " + transfer.getRecipientAccount());
            status.setMessage("Cannot deliver to external account: " + transfer.getRecipientAccount());
            status.setCode(ValidationCode.EXTERNAL_ERROR);
        } else {
            saveOutgoingTransfer(transfer, status);
        }
    }

    private void saveOutgoingTransfer(Transfer transfer, TransferStatus status) {
        LOG.info("Save Outgoing transfer from: " + transfer.getSenderName() + ", to: " + transfer.getRecipientName());
        try {
            Long transferId = DataEntity.execute().saveTransfer(transfer, TransferDirection.OUTGOING);
            DataEntity.execute().decreaseAmount(transfer.getSenderAccount(), transfer.getAmount());
            status.setMessage("ID: " + transferId.toString());
            status.setCode(ValidationCode.OK);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            status.setMessage("Error during save outgoing transfer");
            status.setCode(ValidationCode.SAVING_ERROR);
        }
    }

    private void saveIncomingTransfer(Transfer transfer, TransferStatus status) {
        LOG.info("Save Incoming transfer from: " + transfer.getSenderName() + ", to: " + transfer.getRecipientName());
        try {
            Long transferId = DataEntity.execute().saveTransfer(transfer, TransferDirection.INCOMING);
            DataEntity.execute().increaseAmount(transfer.getRecipientAccount(), transfer.getAmount());
            status.setMessage("ID: " + transferId.toString());
            status.setCode(ValidationCode.OK);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            status.setMessage("Error during save incoming transfer");
            status.setCode(ValidationCode.SAVING_ERROR);
        }
    }

    private void saveInternalTransfer(Transfer transfer, TransferStatus status) {
        LOG.info("Save Internal transfer from: " + transfer.getSenderName() + ", to: " + transfer.getRecipientName());
        try {
            Long transferId = DataEntity.execute().saveTransfer(transfer, TransferDirection.INTERNAL);
            DataEntity.execute().increaseAmount(transfer.getRecipientAccount(), transfer.getAmount());
            DataEntity.execute().decreaseAmount(transfer.getSenderAccount(), transfer.getAmount());
            status.setMessage("ID: " + transferId.toString());
            status.setCode(ValidationCode.OK);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            status.setMessage("Error during save internal transfer");
            status.setCode(ValidationCode.SAVING_ERROR);
        }
    }

}
