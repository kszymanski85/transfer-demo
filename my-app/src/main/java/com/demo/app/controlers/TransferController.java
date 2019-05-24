package com.demo.app.controlers;

import com.demo.app.controlers.model.in.Transfer;
import com.demo.app.controlers.model.in.UserTransfer;
import com.demo.app.services.TransferService;
import com.demo.app.validators.ValidationException;
import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransferController {

    private static final Logger LOG = LoggerFactory.getLogger(TransferController.class);
    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;

    }
    public void getOutgoingTransfers(Javalin request) {
        request.get("/transfer/out/:uid", ctx -> {
            String userId = ctx.pathParam("uid");
            LOG.info("Getting outgoing transfers for User: " + userId);
            ctx.json(transferService.getOutgoingTransfers(userId));
        });
    }

    public void getIncomingTransfers(Javalin request) {
        request.get("/transfer/in/:uid", ctx -> {
            String userId = ctx.pathParam("uid");
            LOG.info("Getting incoming transfers for User: " + userId);
            ctx.json(transferService.getIncomingTransfers(userId));
        });
    }

    public void getTransferDetails(Javalin request) {
        request.get("/transfer/:tid", ctx -> {
            Long transferId = Long.valueOf(ctx.pathParam("tid"));
            LOG.info("Getting details for transfer: " + transferId);
            try {
                ctx.json(transferService.getTransferDetails(transferId));
            } catch(Exception e) {
                LOG.error(e.getMessage());
                ctx.result("Transfer does't exist");
            }
        });
    }

    public void receiveTransfer(Javalin request) {
        request.post("/transfer/ext", ctx -> {
            LOG.info("Getting external transfer");
            try {
                Transfer transfer = ctx.bodyAsClass(Transfer.class);
                ctx.json(transferService.receiveTransfer(transfer));
            } catch(Exception e) {
                LOG.error(e.getMessage());
                ctx.result("Invalid transfer format");
            }
        });
    }

    public void sendTransfer(Javalin request) {
        request.post("/transfer/send", ctx -> {
            String userId = ctx.pathParam("uid");
            LOG.info("Creating transfer for internal user: " + userId);
            try {
                UserTransfer transfer = ctx.bodyAsClass(UserTransfer.class);
                ctx.json(transferService.sendTransfer(userId, transfer));
            } catch(Exception e) {
                LOG.error(e.getMessage());
                ctx.result("Invalid transfer format");
            }
        });
    }

    public void sendPaymentTransfer(Javalin request) {
        request.post("/transfer/pay/:uid", ctx -> {
            String userId = ctx.pathParam("uid");
            LOG.info("Creating payment transfer for " + userId + " creditors");
            try {
                ctx.json(transferService.sendPaymentTransfer(userId));
            } catch(ValidationException e) {
                ctx.json(e.getStatus());
            }
        });
    }


}
