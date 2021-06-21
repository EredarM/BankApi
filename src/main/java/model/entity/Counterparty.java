package model.entity;

import java.util.Objects;

public class Counterparty {
    private int id;
    private String name;
    private String accountNumber;
    private long amount;

    public Counterparty() {

    }

    public Counterparty(String name, String accountNumber, long amount) {
        this.name = name;
        this.accountNumber = accountNumber;
        this.amount = amount;
    }

    public Counterparty(int id, String name, String accountNumber, long amount) {
        this.id = id;
        this.name = name;
        this.accountNumber = accountNumber;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Counterparty that = (Counterparty) o;
        return id == that.id && amount == that.amount && name.equals(that.name) && Objects.equals(accountNumber, that.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, accountNumber, amount);
    }

    @Override
    public String toString() {
        return "Counterparty{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", amount=" + amount +
                '}';
    }
}
