package model.dao;

import model.entity.Counterparty;

import java.sql.SQLException;
import java.util.List;

public interface CounterpartyDao extends DAO<Counterparty>{
    List<Counterparty> getAll() throws SQLException;
}
