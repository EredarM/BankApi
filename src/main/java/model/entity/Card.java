package model.entity;

import java.util.Objects;

public class Card {

    private int id;
    private String cardNumber;
    private String accountNumber;

    public Card(String cardNumber, String accountNumber) {
        this.cardNumber = cardNumber;
        this.accountNumber = accountNumber;
    }

    public Card(int id, String cardNumber, String accountNumber) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.accountNumber = accountNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return id == card.id && Objects.equals(cardNumber, card.cardNumber) && accountNumber.equals(card.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cardNumber, accountNumber);
    }
}
