package com.demo.app;

import com.demo.app.controlers.model.out.User;
import com.demo.app.entity.DAO.CreditorEntity;
import com.demo.app.services.UserService;
import com.demo.app.services.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class UserControllerTest {

    private UserService userService = new UserServiceImpl();

    @Test
    public void getExistingClient() {
        String userId = "janko22";
        User user = userService.getUser(userId);
        Assertions.assertNotNull(user);
        Assertions.assertEquals(user.getUserId(), userId);
    }

    @Test
    public void getInexistingClient() {
        String userId = "IamNotExist";
        User user = userService.getUser(userId);
        Assertions.assertNull(user);
    }

    @Test
    public void getCreditorsForUserWhoIsNotCompany() {
        String userId = "janko22";
        List<CreditorEntity> creditors = userService.getCreditors(userId);
        Assertions.assertTrue(creditors.isEmpty());
    }

    @Test
    public void getCreditorsForCompanyWhoHasNotCreditors() {
        String userId = "tinyS";
        List<CreditorEntity> creditors = userService.getCreditors(userId);
        Assertions.assertTrue(creditors.isEmpty());
    }

    @Test
    public void getCreditorsForCompanyWhoHasAssignedCreditors() {
        String userId = "lotos";
        List<CreditorEntity> creditors = userService.getCreditors(userId);
        Assertions.assertEquals(creditors.size(), 4);
    }

}
