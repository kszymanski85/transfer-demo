package com.demo.app.services;

import com.demo.app.controlers.model.out.User;
import com.demo.app.entity.DAO.CreditorEntity;
import com.demo.app.entity.DAO.UserEntity;
import com.demo.app.entity.DataEntity;

import java.util.List;

import static java.util.Objects.isNull;

public class UserServiceImpl implements UserService {

    public User getUser(String userId) {
        UserEntity userEntity = DataEntity.execute().getUser(userId);
        return isNull(userEntity) ? null : new User (userEntity.getUserId(), getFullName(userEntity), userEntity.getAddress(), userEntity.getPhoneNumber(),
                userEntity.getAmount(), userEntity.getAccountNumber(), userEntity.getIsCompany());
    }

    public List<CreditorEntity> getCreditors(String userId) {
        return DataEntity.execute().getCreditors(userId);
    }

    private String getFullName(UserEntity userEntity) {
        return userEntity.getName() + " " + userEntity.getSurname();
    }

}
