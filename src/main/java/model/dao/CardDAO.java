package model.dao;

import model.entity.Card;

import java.sql.SQLException;
import java.util.List;

public interface CardDAO extends DAO<Card> {
    List<Card> getAllCard(String accountNumber) throws SQLException;
}
