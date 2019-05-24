package com.demo.app;

import com.demo.app.controlers.TransferController;
import com.demo.app.controlers.UserController;
import com.demo.app.services.TransferServiceImpl;
import com.demo.app.services.UserServiceImpl;
import com.demo.app.validators.ValidatorImpl;
import io.javalin.Javalin;

public class App 
{
    public static void main( String[] args ) {
        Javalin app = Javalin.create().start(7000);
        app.get("/", ctx -> ctx.result("Main Page"));

        UserController userController = new UserController(new UserServiceImpl());
            userController.getUserDetails(app);
            userController.getCreditors(app);
        TransferController transferController = new TransferController(new TransferServiceImpl(new ValidatorImpl()));
            transferController.getIncomingTransfers(app);
            transferController.getOutgoingTransfers(app);
            transferController.getTransferDetails(app);
            transferController.receiveTransfer(app);
            transferController.sendTransfer(app);
            transferController.sendPaymentTransfer(app);
    }
}
