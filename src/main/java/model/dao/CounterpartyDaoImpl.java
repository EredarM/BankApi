package model.dao;

import model.entity.BankAccount;
import model.entity.Counterparty;
import supportClass.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CounterpartyDaoImpl implements CounterpartyDao{
    private Connection connection;

    public CounterpartyDaoImpl() {
        connection = ConnectionFactory.getConnection();
    }

    @Override
    public List<Counterparty> getAll() throws SQLException{
        String sqlQuery = "SELECT * FROM counterparty";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sqlQuery);
        List<Counterparty> counterpartyList = new ArrayList<>();
        while (resultSet.next()) {
            counterpartyList.add(new Counterparty(resultSet.getInt("id"),
                    resultSet.getString("name"), resultSet.getString("accountNumber"), resultSet.getLong("amount")));
        }
        statement.close();
        return counterpartyList;
    }

    @Override
    public void update(Counterparty counterparty) throws SQLException {
        String sqlQuery = "update counterparty set name = ?, accountNumber = ?, amount = ? where id = ?";
        PreparedStatement statement = connection.prepareStatement(sqlQuery);
        statement.setString(1, counterparty.getName());
        statement.setString(2, counterparty.getAccountNumber());
        statement.setLong(3, counterparty.getAmount());
        statement.setInt(4, counterparty.getId());
        statement.execute();
        statement.close();
    }

    @Override
    public void save(Counterparty counterparty) throws SQLException {
        String sqlQuery = "insert into counterparty (name, accountnumber, amount) values (?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sqlQuery);
        statement.setString(1, counterparty.getName());
        statement.setString(2, counterparty.getAccountNumber());
        statement.setLong(3, counterparty.getAmount());
        statement.execute();
        statement.close();
    }

    @Override
    public Counterparty getByNumber(String number) throws SQLException {
        String sqlQuery = "select * from counterparty where accountNumber = ?";
        PreparedStatement statement = connection.prepareStatement(sqlQuery);
        statement.setString(1, number);
        ResultSet resultSet = statement.executeQuery();
        Counterparty counterparty = null;
        while (resultSet.next()) {
            counterparty = new Counterparty(resultSet.getInt("id"),
                    resultSet.getString("name"), resultSet.getString("accountNumber"), resultSet.getLong("amount"));
        }
        return counterparty;
    }
}
