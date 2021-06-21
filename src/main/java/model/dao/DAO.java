package model.dao;

import model.entity.BankAccount;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface DAO<T> {
    void update(T t) throws SQLException;

    void save(T t) throws SQLException;

    T getByNumber(String number) throws SQLException;
}
