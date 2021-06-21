package model.dao;

import model.entity.BankAccount;
import supportClass.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class BankAccountDaoImp implements DAO<BankAccount> {
    private Connection connection;

    public BankAccountDaoImp() {
        connection = ConnectionFactory.getConnection();
    }

    @Override
    public void update(BankAccount bankAccount) throws SQLException {
        String sqlQuery = "update bankAccount set clientId = ?, accountnumber = ?, amount = ? where accountnumber = ?";
        PreparedStatement statement = connection.prepareStatement(sqlQuery);
        statement.setInt(1, bankAccount.getClientId());
        statement.setString(2, bankAccount.getAccountNumber());
        statement.setLong(3, bankAccount.getAmount());
        statement.setString(4, bankAccount.getAccountNumber());
        statement.execute();
        statement.close();
    }


    @Override
    public void save(BankAccount bankAccount) throws SQLException {
        String sqlQuery = "insert into bankaccount (clientId, accountnumber, amount) values (?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sqlQuery);
        statement.setInt(1, bankAccount.getClientId());
        statement.setString(2, bankAccount.getAccountNumber());
        statement.setLong(3, bankAccount.getAmount());
        statement.execute();
        statement.close();

    }

    @Override
    public BankAccount getByNumber(String accountNumber) throws SQLException {
        String sqlQuery = "select * from bankaccount where accountNumber = ?";
        PreparedStatement statement = connection.prepareStatement(sqlQuery);
        statement.setString(1, accountNumber);
        ResultSet resultSet = statement.executeQuery();
        BankAccount bankAccount = null;
        while (resultSet.next()) {
            bankAccount = new BankAccount(resultSet.getInt("id"),
                    resultSet.getString("accountnumber"), resultSet.getLong("amount"), resultSet.getInt("clientid"));
        }
        return bankAccount;
    }
}
