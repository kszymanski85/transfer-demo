package com.demo.app.validators;

import com.demo.app.controlers.model.in.Transfer;
import com.demo.app.controlers.model.in.UserTransfer;
import com.demo.app.entity.DAO.AccountEntity;
import com.demo.app.entity.DAO.CreditorEntity;
import com.demo.app.entity.DAO.UserEntity;
import com.demo.app.controlers.model.out.TransferStatus;

import java.util.List;

public interface Validator {
    Validator validate(UserEntity user);
    Validator validate(UserTransfer userTransfer);
    Validator validate(AccountEntity account);
    Validator validate(Transfer transfer);
    Validator validate(List<CreditorEntity> creditors);
    ValidatorImpl init();
    TransferStatus run();
}
