package com.demo.app.services;

import com.demo.app.controlers.model.in.Transfer;
import com.demo.app.controlers.model.in.UserTransfer;
import com.demo.app.controlers.model.out.PaymentStatus;
import com.demo.app.controlers.model.out.TransferOverview;
import com.demo.app.entity.DAO.TransferEntity;
import com.demo.app.controlers.model.out.TransferStatus;
import com.demo.app.validators.ValidationException;

import java.util.List;

public interface TransferService {
    List<TransferOverview> getOutgoingTransfers(String userId);
    List<TransferOverview> getIncomingTransfers(String userId);
    TransferEntity getTransferDetails(Long transferId);
    TransferStatus receiveTransfer(Transfer transfer);
    TransferStatus sendTransfer(String userId, UserTransfer transfer);
    PaymentStatus sendPaymentTransfer(String userId) throws ValidationException;
}
