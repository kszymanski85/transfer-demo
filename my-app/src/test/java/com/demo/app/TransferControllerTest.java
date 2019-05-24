package com.demo.app;

import com.demo.app.controlers.model.config.ValidationCode;
import com.demo.app.controlers.model.in.Transfer;
import com.demo.app.controlers.model.in.UserTransfer;
import com.demo.app.controlers.model.out.PaymentStatus;
import com.demo.app.controlers.model.out.TransferOverview;
import com.demo.app.controlers.model.out.TransferStatus;
import com.demo.app.entity.DAO.TransferEntity;
import com.demo.app.services.TransferService;
import com.demo.app.services.TransferServiceImpl;
import com.demo.app.services.UserService;
import com.demo.app.services.UserServiceImpl;
import com.demo.app.validators.ValidationException;
import com.demo.app.validators.Validator;
import com.demo.app.validators.ValidatorImpl;
import org.junit.jupiter.api.*;

import java.math.BigInteger;
import java.util.List;

public class TransferControllerTest {

    private Validator validator = new ValidatorImpl();
    private UserService userService = new UserServiceImpl();
    private TransferService transferService = new TransferServiceImpl(validator);

    @Nested
    @DisplayName("Send Payment Transfers")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class PaymentTransfers {

        @Test
        @Order(1)
        @DisplayName("Should return user validation error")
        public void sendPaymentTransferAsNullClient() {
            String userId = "NotExistingUser";
            try{
                transferService.sendPaymentTransfer(userId);
            } catch (ValidationException ext) {
                Assertions.assertEquals(ext.getStatus().getMessage(), "User doesn't exist");
                Assertions.assertEquals(ext.getStatus().getCode(), ValidationCode.USER_ERROR);
            }
        }

        @Test
        @Order(2)
        @DisplayName("Should return cause user is not a company")
        public void sendPaymentTransferAsSimpleUser() {
            String userId = "janko22";
            try{
                transferService.sendPaymentTransfer(userId);
            } catch (ValidationException ext) {
                Assertions.assertEquals(ext.getStatus().getMessage(), "User is not company");
                Assertions.assertEquals(ext.getStatus().getCode(), ValidationCode.PAYMENT_TRANSFER_ERROR);
            }
        }

        @Test
        @Order(3)
        @DisplayName("Should return cause company has no assigned creditors")
        public void sendPaymentTransferAsCompanyWithoutCreditors() {
            String userId = "tinyS";
            try{
                transferService.sendPaymentTransfer(userId);
            } catch (ValidationException ext) {
                Assertions.assertEquals(ext.getStatus().getMessage(), "No assigned creditors");
                Assertions.assertEquals(ext.getStatus().getCode(), ValidationCode.PAYMENT_TRANSFER_ERROR);
            }
        }

        @Test
        @Order(4)
        @DisplayName("Should return cause company has no enough resources")
        public void sendPaymentWhenHaveNoResources() {
            String userId = "lotos";
            try{
                transferService.sendPaymentTransfer(userId);
            } catch (ValidationException ext) {
                Assertions.assertEquals(ext.getStatus().getMessage(), "Amount is not enough to complete all transfers");
                Assertions.assertEquals(ext.getStatus().getCode(), ValidationCode.PAYMENT_TRANSFER_ERROR);
            }
        }

        @Test
        @Order(5)
        @DisplayName("Should send couple of ransfers successfully and one failed")
        public void sendOneOfPaymentsFailedToExternal() {
            String userId = "lotos";
            try{
                PaymentStatus status = transferService.sendPaymentTransfer(userId);
                Assertions.assertEquals(status.getStatus(), "PARTIALLY-COMPLETED");
                Assertions.assertEquals(status.getCompleted().size(), 3);
                Assertions.assertEquals(status.getFailed().size(), 1);
            } catch (ValidationException ext) {
                Assertions.assertEquals(ext.getStatus().getMessage(), "Anything I'm not assert that");
            }
        }

        @Test
        @Order(6)
        @DisplayName("Should send all transfers successfully")
        public void sendAllTransfersSuccessfully() {
            String userId = "orlen";
            try{
                PaymentStatus status = transferService.sendPaymentTransfer(userId);
                Assertions.assertEquals(status.getStatus(), "ALL COMPLETED");
                Assertions.assertEquals(status.getCompleted().size(), 2);
                Assertions.assertTrue(status.getFailed().isEmpty());
            } catch (ValidationException ext) {
                Assertions.assertEquals(ext.getStatus().getMessage(), "Anything cause I'm not assert that");
            }
        }

    }

    @Nested
    @DisplayName("Send transfers as User")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class SendUserTransfer {

        @Test
        @Order(1)
        @DisplayName("Should return error by sender user doesn't exist")
        public void sendTransferAsBadUser() {
            String userId = "NotExisting";
            UserTransfer transfer = new UserTransfer(200L, "Jack Frost", new BigInteger("1234567890123456"), "Olsztyn 123", "Snieg");
            TransferStatus status = transferService.sendTransfer(userId, transfer);
            Assertions.assertEquals(status.getMessage(), "User doesn't exist");
            Assertions.assertEquals(status.getCode(), ValidationCode.USER_ERROR);
        }

        @Test
        @Order(2)
        @DisplayName("Should return error cause not enough money")
        public void checkAccountResources() {
            String userId = "wimar";
            UserTransfer transfer = new UserTransfer(20000L, "Jack Frost", new BigInteger("1234567890123456"), "Olsztyn 123", "Snieg");
            TransferStatus status = transferService.sendTransfer(userId, transfer);
            Assertions.assertEquals(status.getMessage(), "You don't have enough resources to sent the transfer");
            Assertions.assertEquals(status.getCode(), ValidationCode.USER_TRANSFER_ERROR);
        }

        @Test
        @Order(3)
        @DisplayName("Should return error cause recipient Account length is different than 16 characters")
        public void checkRecipientAccountNumberLength() {
            String userId = "lotos";
            UserTransfer transfer = new UserTransfer(200L, "Jack Frost", new BigInteger("123456789012345612"), "Olsztyn 123", "Snieg");
            TransferStatus status = transferService.sendTransfer(userId, transfer);
            Assertions.assertEquals(status.getMessage(), "Invalid recipient account number length");
            Assertions.assertEquals(status.getCode(), ValidationCode.USER_TRANSFER_ERROR);
        }

        @Test
        @Order(4)
        @DisplayName("Should return error cause recipient Address length is less than 5 characters")
        public void checkRecipientAdressLength() {
            String userId = "wimar";
            UserTransfer transfer = new UserTransfer(200L, "Jack Frost", new BigInteger("1234567890123456"), "ABCD", "Snieg");
            TransferStatus status = transferService.sendTransfer(userId, transfer);
            Assertions.assertEquals(status.getMessage(), "Recipient address should contain almost 5 characters");
            Assertions.assertEquals(status.getCode(), ValidationCode.USER_TRANSFER_ERROR);
        }

        @Test
        @Order(5)
        @DisplayName("Should return error cause Descriptions length is less than 5 characters")
        public void checkDescriptionLength() {
            String userId = "wimar";
            UserTransfer transfer = new UserTransfer(200L, "Janosik", new BigInteger("1234567890123456"), "Krakow 123", "XXX");
            TransferStatus status = transferService.sendTransfer(userId, transfer);
            Assertions.assertEquals(status.getMessage(), "Description should contain almost 5 characters");
            Assertions.assertEquals(status.getCode(), ValidationCode.USER_TRANSFER_ERROR);
        }

        @Test
        @Order(6)
        @DisplayName("Should return error cause Account locked")
        public void checkIsAccountLocked() {
            String userId = "luko_55";
            UserTransfer transfer = new UserTransfer(200L, "Janosik", new BigInteger("1234567890123456"), "Krakow 123", "Cos tam");
            TransferStatus status = transferService.sendTransfer(userId, transfer);
            Assertions.assertEquals(status.getMessage(), "Account is closed");
            Assertions.assertEquals(status.getCode(), ValidationCode.ACCOUNT_ERROR);
        }

        @Test
        @Order(7)
        @DisplayName("Should return transfer status OK, without error message")
        public void sendProperExternalTransfer() {
            String userId = "tinyS";
            UserTransfer transfer = new UserTransfer(200L, "Batman", new BigInteger("1234567890123456"), "Krakow 123", "Cos tam");
            TransferStatus status = transferService.sendTransfer(userId, transfer);
            Assertions.assertTrue(status.getMessage().contains("ID:"));
            Assertions.assertEquals(status.getCode(), ValidationCode.OK);
        }

        @Test
        @Order(8)
        @DisplayName("Should return transfer status OK, without error message")
        public void sendProperInternalTransfer() {
            String userId = "wimar";
            UserTransfer transfer = new UserTransfer(500L, "Janek", new BigInteger("1111222200002222"), "Krakow", "Przelew");
            TransferStatus status = transferService.sendTransfer(userId, transfer);
            Assertions.assertTrue(status.getMessage().contains("ID:"));
            Assertions.assertEquals(status.getCode(), ValidationCode.OK);
        }

    }

    @Nested
    @DisplayName("Send transfers as external provider")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class ReceiveTransfer {

        @Test
        @Order(1)
        @DisplayName("Should return validation error by empty input")
        public void sendEmptyTransfer() {
            TransferStatus status = transferService.receiveTransfer(null);
            Assertions.assertEquals(status.getMessage(), "Transfer doesn't exist");
            Assertions.assertEquals(status.getCode(), ValidationCode.TRANSFER_ERROR);
        }

        @Test
        @Order(2)
        @DisplayName("Should return validation error cause sender name has les than 5 character")
        public void sendTransferWithSenderNameShorterThanFive() {
            Transfer transfer = new Transfer(200L, "Pan Janek", new BigInteger("1111222200002222"), "Krakow 12",
                    "Pit", new BigInteger("6765678987456345"), "Gdanska 123", "Dysk SSD");
            TransferStatus status = transferService.receiveTransfer(transfer);
            Assertions.assertEquals(status.getMessage(), "Sender name need to has almost 5 character");
            Assertions.assertEquals(status.getCode(), ValidationCode.TRANSFER_ERROR);
        }

        @Test
        @Order(3)
        @DisplayName("Should return validation error cause description has les than 5 character")
        public void sendTransferWithDescriptionShorterThanFive() {
            Transfer transfer = new Transfer(200L, "Pan Janek", new BigInteger("1111222200002222"), "Krakow 12",
                    "Piter Parker", new BigInteger("6765678987456345"), "Gdanska 123", "SSD");
            TransferStatus status = transferService.receiveTransfer(transfer);
            Assertions.assertEquals(status.getMessage(), "Description need to has almost 5 character");
            Assertions.assertEquals(status.getCode(), ValidationCode.TRANSFER_ERROR);
        }

        @Test
        @Order(4)
        @DisplayName("Should return validation error cause account name has other length than 16 character")
        public void sendTransferWithRecipientAccountNumberLengthNotMatch() {
            Transfer transfer = new Transfer(200L, "Pan Janek", new BigInteger("11112222000022"), "Krakow 12",
                    "Piter Parker", new BigInteger("6765678987456345"), "Gdanska 123", "Dysk SSD");
            TransferStatus status = transferService.receiveTransfer(transfer);
            Assertions.assertEquals(status.getMessage(), "Destination account length is not match");
            Assertions.assertEquals(status.getCode(), ValidationCode.TRANSFER_ERROR);
        }

        @Test
        @Order(5)
        @DisplayName("Should save incoming transfer and return transfer id")
        public void sendProperTransfer() {
            Transfer transfer = new Transfer(200L, "Pan Janek", new BigInteger("1111222200002222"), "Krakow 12",
                    "Piter Parker", new BigInteger("6765678987456345"), "Gdanska 123", "Dysk SSD");
            TransferStatus status = transferService.receiveTransfer(transfer);
            Assertions.assertTrue(status.getMessage().contains("ID:"));
            Assertions.assertEquals(status.getCode(), ValidationCode.OK);
        }

    }

    @Nested
    @DisplayName("List transfers")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class ListTransfers {

        @Test
        @Order(1)
        @DisplayName("Should return empty list of Outgoing transfers")
        public void getOutgoingTransfersForUserWhoHasNone() {
            String userId = "lotos";
            List<TransferOverview> transfers = transferService.getOutgoingTransfers(userId);
            Assertions.assertTrue(transfers.isEmpty());
        }

        @Test
        @Order(2)
        @DisplayName("Should return two Outgoing transfers")
        public void getOutgoingTransfersForUserWhoHasTransfers() {
            String userId = "wimar";
            List<TransferOverview> transfers = transferService.getOutgoingTransfers(userId);
            Assertions.assertEquals(transfers.size(), 2);
        }

        @Test
        @Order(3)
        @DisplayName("Should return empty list Incoming transfers")
        public void getIncomingTransfersForUserWhoHasNone() {
            String userId = "lotos";
            List<TransferOverview> transfers = transferService.getIncomingTransfers(userId);
            Assertions.assertTrue(transfers.isEmpty());
        }

        @Test
        @Order(4)
        @DisplayName("Should return 2 Incoming transfers")
        public void getIncomingTransfersForUserWhoHasTransfers() {
            String userId = "janko22";
            List<TransferOverview> transfers = transferService.getIncomingTransfers(userId);
            Assertions.assertEquals(transfers.size(), 2);
        }

    }

    @Nested
    @DisplayName("Get transfer details")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class TransferDetails {

        @Test
        @DisplayName("Should return no transfer")
        @Order(1)
        public void getTransferDetailsForFakeId() {
            Long transferId = 99999999L;
            TransferEntity transfer = transferService.getTransferDetails(transferId);
            Assertions.assertNull(transfer);
        }

        @Test
        @DisplayName("Should return transfer")
        @Order(2)
        public void getTransferDetailsForExistingId() {
            Long transferId = 111L;
            TransferEntity transfer = transferService.getTransferDetails(transferId);
            Assertions.assertNotNull(transfer);
            Assertions.assertEquals(transfer.getSenderName(), "Marek Wisniowski");
            Assertions.assertEquals(transfer.getRecipientName(), "Pan Gienek");
        }

    }

}
