package com.demo.app.entity;

import com.demo.app.controlers.model.in.Transfer;
import com.demo.app.entity.DAO.*;
import com.demo.app.entity.config.TransferDirection;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class DataEntity {

    private static DataEntity instance = null;

    private Long transferId;

    private List<UserEntity> users;
    private List<AccountEntity> accounts;
    private List<CreditorEntity> creditors;
    private List<TransferEntity> transfers;

    private DataEntity() {
        insertTestData();
        transferId = 1000L;
    }

    public static DataEntity execute() {
        if ( instance == null ) {
            instance = new DataEntity();
        }
        return instance;
    }

    public UserEntity getUser(String userId) {
        AccountEntity account = getAccountByUserId(userId);
        List<UserEntity> entity = users.stream()
                .filter(usr -> usr.getUserId().equals(userId))
                .map(usr -> usr.setAccountData(account))
                .collect(Collectors.toList());
        return entity.isEmpty() ? null : entity.get(0);
    }

    public TransferEntity getTransferDetails(Long transferId) {
        List<TransferEntity> entity = transfers.stream()
                .filter(tsf -> tsf.getTransferId().equals(transferId))
                .collect(Collectors.toList());
        return entity.isEmpty() ? null : entity.get(0);
    }

    public List<TransferEntity> getOutgoingTransfers(String userId) {
        AccountEntity accNr = getAccountByUserId(userId);
        return isNull(accNr) ? null : transfers.stream()
                .filter(tfr -> tfr.getSenderAccount().equals(accNr.getAccountNumber()) &&
                        (tfr.getDirection().equals(TransferDirection.OUTGOING) || tfr.getDirection().equals(TransferDirection.INTERNAL)))
                .collect(Collectors.toList());
    }

    public List<TransferEntity> getIncomingTransfers(String userId) {
        AccountEntity accNr = getAccountByUserId(userId);
        return isNull(accNr) ? null : transfers.stream()
                .filter(tfr -> tfr.getRecipientAccount().equals(accNr.getAccountNumber()) &&
                        (tfr.getDirection().equals(TransferDirection.INCOMING) || tfr.getDirection().equals(TransferDirection.INTERNAL)))
                .collect(Collectors.toList());
    }

    public List<CreditorEntity> getCreditors(String userId) {
        UserEntity user = getUser(userId);
        return isNull(user) ? null : creditors.stream()
                .filter(cds -> cds.getCompanyReference().equals(user.getCreditorReferenceId()))
                .collect(Collectors.toList());
    }

    public AccountEntity getAccountByNumber(BigInteger accountNumber) {
        List<AccountEntity> entities = accounts.stream()
                .filter(acc -> acc.getAccountNumber().equals(accountNumber))
                .collect(Collectors.toList());
        return entities.isEmpty() ? null : entities.get(0);
    }

    public AccountEntity getAccountByUserId(String userId) {
        List<AccountEntity> entity = accounts.stream()
                .filter(acc -> acc.getOwnerId().equals(userId))
                .collect(Collectors.toList());
        return entity.isEmpty() ? null : entity.get(0);
    }

    public void increaseAmount(BigInteger accountNumber, Long value) {
        accounts.forEach(acc -> {
            if(acc.getAccountNumber().equals(accountNumber)) {
                acc.increase(value);
            }
        });
    }

    public void decreaseAmount(BigInteger accountNumber, Long value) {
        accounts.forEach(acc -> {
            if(acc.getAccountNumber().equals(accountNumber)) {
                acc.decrease(value);
            }
        });
    }

    public Long saveTransfer(Transfer transfer, TransferDirection direction) {
        TransferEntity transferEntity = new TransferEntity(transferId, transfer.getAmount(), transfer.getRecipientName(),
                transfer.getRecipientAccount(),transfer.getRecipientAddress(), transfer.getSenderName(), transfer.getSenderAccount(),
                transfer.getSenderAddress(), transfer.getDescription(), direction, LocalDateTime.now());
        transferId += 1;
        transfers.add(transferEntity);
        return transferEntity.getTransferId();
    }

    private void insertTestData() {
        UserEntity user1 = new UserEntity("janko22", "Jan", "Kowalski","Krakow 111", 48111222333L, false, 0L);
        UserEntity user2 = new UserEntity("wimar", "Marek", "Wisniowski","Wieliczka 12", 48222333444L, false, 0L);
        UserEntity user3 = new UserEntity("luko_55", "Lukasz", "Konieczny","Warszawa 34", 48444555666L, false, 0L);

        UserEntity company1 = new UserEntity("lotos", "Lotos S.A.", "","Warszawa 11", 48123123123L, true, 1122L);
        CreditorEntity creditor1 = new CreditorEntity(1122L, "Jan Kowalski", "Wieliczka 11", new BigInteger("1111222200002222"), 2000L);
        CreditorEntity creditor2 = new CreditorEntity(1122L, "FaiExternal", "Other galaxy", new BigInteger("5554442221113344"), 1000L);
        CreditorEntity creditor3 = new CreditorEntity(1122L, "Jacek Kaminski", "Wieliczka 22", new BigInteger("9234873400002456"), 2000L);
        CreditorEntity creditor4 = new CreditorEntity(1122L, "Startup", "Wieliczka 33", new BigInteger("1111777700007777"), 7000L);

        UserEntity company2 = new UserEntity("orlen", "Orlen Rafinery", "","Krakow 22", 48334445556L, true, 3344L);
        CreditorEntity creditor5= new CreditorEntity(3344L, "Maciej Laskowski", "Wrocla 11", new BigInteger("8345345600003456"), 3000L);
        CreditorEntity creditor6 = new CreditorEntity(3344L, "OurStartup", "Poznan 31", new BigInteger("1111777700007777"), 5000L);

        UserEntity company3 = new UserEntity("tinyS", "MyStartup", "","Krakow 55", 48987654321L, true, 0L);

        AccountEntity account1 = new AccountEntity("janko22", new BigInteger("1111222200002222"), 3000L, false);
        AccountEntity account2 = new AccountEntity("wimar", new BigInteger("1111333300003333"), 4000L, false);
        AccountEntity account3 = new AccountEntity("luko_55", new BigInteger("1111444400004444"), 5000L, true);
        AccountEntity account4 = new AccountEntity("lotos", new BigInteger("1111555500005555"), 50000L, false);
        AccountEntity account5 = new AccountEntity("orlen", new BigInteger("1111666600006666"), 10000L, false);
        AccountEntity account6 = new AccountEntity("tinyS", new BigInteger("1111777700007777"), 3000L, false);

        TransferEntity transfer1 = new TransferEntity(111L, 200L, "Pan Gienek", new BigInteger("1234859601295462"),
                "Zakopane 334", "Marek Wisniowski", new BigInteger("1111333300003333"), "Wieliczka 12",
                "ksiazki", TransferDirection.OUTGOING, LocalDateTime.of(2018, 2, 11, 10, 15));

        TransferEntity transfer2 = new TransferEntity(222L, 500L, "Janek z Krakowa", new BigInteger("1111222200002222"),
                "Krakow jakas tam ulica", "Pan Gienek", new BigInteger("1234859601295462"), "Zakopana 334",
                "pozyczka", TransferDirection.INCOMING, LocalDateTime.of(2018, 2, 11, 11, 15));

        TransferEntity transfer3 = new TransferEntity(333L, 50L, "Janek Kowalski", new BigInteger("1111222200002222"),
                "Krakow", "Marek Wisniowski", new BigInteger("1111333300003333"), "Wieliczka 12",
                "za obiad", TransferDirection.INTERNAL, LocalDateTime.of(2018, 2, 11, 10, 15));

        users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(company1);
        users.add(company2);
        users.add(company3);

        accounts = new ArrayList<>();
        accounts.add(account1);
        accounts.add(account2);
        accounts.add(account3);
        accounts.add(account4);
        accounts.add(account5);
        accounts.add(account6);

        creditors = new ArrayList<>();
        creditors.add(creditor1);
        creditors.add(creditor2);
        creditors.add(creditor3);
        creditors.add(creditor4);
        creditors.add(creditor5);
        creditors.add(creditor6);

        transfers = new ArrayList<>();
        transfers.add(transfer1);
        transfers.add(transfer2);
        transfers.add(transfer3);
    }

}
