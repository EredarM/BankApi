package model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BankAccount {
    private int id;
    private String accountNumber;
    private long amount;
    private int clientId;
    private List<Card> cardNumber;

    public BankAccount(int id, String accountNumber, long amount, int clientId) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.clientId = clientId;
        this.cardNumber = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public List<Card> getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(List<Card> cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccount that = (BankAccount) o;
        return id == that.id && amount == that.amount && clientId == that.clientId && accountNumber.equals(that.accountNumber) && Objects.equals(cardNumber, that.cardNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountNumber, amount, clientId, cardNumber);
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "id=" + id +
                ", accountNumber='" + accountNumber + '\'' +
                ", amount=" + amount +
                ", clientId=" + clientId +
                ", cardNumber=" + cardNumber +
                '}';
    }
}
