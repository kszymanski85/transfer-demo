package com.demo.app.controlers;

import com.demo.app.services.UserService;
import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public void getUserDetails(Javalin request) {
        request.get("/user/:id", ctx -> {
            String userId = ctx.pathParam("id");
            LOG.info("Getting details for User: " + userId);
            try {
                ctx.json(userService.getUser(userId));
            } catch(Exception e) {
                LOG.error(e.getMessage());
                ctx.result("User doesn't exist");
            }
        });
    }

    public void getCreditors(Javalin request) {
        request.get("/creditors/:id", ctx -> {
            String userId = ctx.pathParam("id");
            LOG.info("Getting creditors for User: " + userId);
            try {
                ctx.json(userService.getCreditors(userId));
            } catch(Exception e) {
                LOG.error(e.getMessage());
                ctx.result("No assigned creditors");
            }
        });
    }

}
