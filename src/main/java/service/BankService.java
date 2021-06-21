package service;

import supportClass.ResponseEntity;

import java.io.InputStream;

public interface BankService {
    ResponseEntity createNewCard(InputStream inputStream);

    ResponseEntity getCardList(String paramString);

    ResponseEntity deposit(InputStream inputStream);

    ResponseEntity getBalance(String paramString);

    ResponseEntity addCounterparty(InputStream inputStream);

    ResponseEntity getCounterParty();

    ResponseEntity depositToCounterParty(InputStream inputStream);
}
