package com.demo.app.services;

import com.demo.app.controlers.model.out.User;
import com.demo.app.entity.DAO.CreditorEntity;

import java.util.List;

public interface UserService {
    User getUser(String userId);
    List<CreditorEntity> getCreditors(String userId);
}
