package model.dao;

import model.entity.Card;
import supportClass.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CardDaoImp implements CardDAO {
    private Connection connection;

    public CardDaoImp() {
        connection = ConnectionFactory.getConnection();
    }

    @Override
    public void update(Card card) throws SQLException {
        String sqlQuery = "update Card set cardNumber = ?, accountNumber = ? where id = ?";
        PreparedStatement statement = connection.prepareStatement(sqlQuery);
        statement.setString(1, card.getCardNumber());
        statement.setString(2, card.getAccountNumber());
        statement.setInt(3, card.getId());
        statement.execute();
        statement.close();
    }

    @Override
    public void save(Card card) throws SQLException {
        String sqlQuery = "insert into card (cardnumber, accountnumber) values (?, ?)";
        PreparedStatement statement = connection.prepareStatement(sqlQuery);
        statement.setString(1, card.getCardNumber());
        statement.setString(2, card.getAccountNumber());
        System.out.println(statement);
        statement.execute();
        statement.close();
    }

    @Override
    public Card getByNumber(String numberCard) throws SQLException {
        String sqlQuery = "select * from card where cardnumber = ?";
        PreparedStatement statement = connection.prepareStatement(sqlQuery);
        statement.setString(1, numberCard);
        ResultSet resultSet = statement.executeQuery();
        Card card = null;
        while (resultSet.next()) {
            card = new Card(resultSet.getString(2), resultSet.getString(3));
            card.setId(resultSet.getInt(1));
            return card;
        }
        return card;
    }

    @Override
    public List<Card> getAllCard(String accountNumber) throws SQLException {
        String sqlQuery = "select * from card where accountNumber = ?";
        PreparedStatement statement = connection.prepareStatement(sqlQuery);
        statement.setString(1, accountNumber);
        ResultSet resultSet = statement.executeQuery();
        List<Card> cardList = new ArrayList<>();
        while (resultSet.next()) {
            Card card = new Card(resultSet.getInt("id"), resultSet.getString("cardNumber"), resultSet.getString("accountNumber"));
            cardList.add(card);
        }
        return cardList;
    }
}
