import junit.framework.TestCase;
import model.dao.CounterpartyDao;
import model.dao.CounterpartyDaoImpl;
import model.entity.Card;
import model.entity.Counterparty;
import org.apache.ibatis.jdbc.ScriptRunner;
import supportClass.ConnectionFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DaoCounterpartyTest extends TestCase {
    private CounterpartyDao counterpartyDao;
    private Connection connection;

    private Counterparty expectedCounterparty1;
    private Counterparty expectedCounterparty2;
    @Override
    public void setUp() throws Exception {
        String url = "jdbc:h2:/Users/a19215121/IdeaProjects/BankApi/src/main/resources/testBank;AUTO_SERVER=TRUE";
        String user = "root";
        String password = "testtest";
        connection = ConnectionFactory.getConnection(url, user, password);
        ScriptRunner scriptRunner = new ScriptRunner(connection);
        counterpartyDao = new CounterpartyDaoImpl();
        Reader reader = new BufferedReader(new FileReader("src/main/resources/testsDBScripts/createTableCounterpartyScript.sql"));
        scriptRunner.runScript(reader);

        expectedCounterparty1 = new Counterparty(1, "first", "5555", 40000);
        expectedCounterparty2 = new Counterparty(2, "secodn", "7777", 100000);

    }

    public void testGetAllAccountCounterparty() throws SQLException {
        String sqlQuery = "insert into counterparty (name, accountnumber, amount) values (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        preparedStatement.setString(1, expectedCounterparty1.getName());
        preparedStatement.setString(2, expectedCounterparty1.getAccountNumber());
        preparedStatement.setLong(3, expectedCounterparty1.getAmount());
        preparedStatement.execute();
        preparedStatement.setString(1, expectedCounterparty2.getName());
        preparedStatement.setString(2, expectedCounterparty2.getAccountNumber());
        preparedStatement.setLong(3, expectedCounterparty2.getAmount());
        preparedStatement.execute();

        List<Counterparty> expectedCounterpartyList = new ArrayList<>();
        expectedCounterpartyList.add(expectedCounterparty1);
        expectedCounterpartyList.add(expectedCounterparty2);

        assertEquals(expectedCounterpartyList, counterpartyDao.getAll());
    }

    public void testUpdateCounterparty() throws SQLException {
        String sqlQuery = "insert into counterparty (name, accountnumber, amount) values (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        preparedStatement.setString(1, expectedCounterparty1.getName());
        preparedStatement.setString(2, expectedCounterparty1.getAccountNumber());
        preparedStatement.setLong(3, expectedCounterparty1.getAmount());
        preparedStatement.execute();
        preparedStatement.close();

        expectedCounterparty1.setAmount(1111);
        expectedCounterparty1.setAccountNumber("1111");
        expectedCounterparty1.setName("test");

        counterpartyDao.update(expectedCounterparty1);

        sqlQuery = "SELECT * FROM counterparty";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sqlQuery);

        Counterparty actualCounterparty = null;
        if (resultSet.next()) {
            actualCounterparty = new Counterparty(resultSet.getInt("id"),
                    resultSet.getString("name"), resultSet.getString("accountNumber"), resultSet.getLong("amount"));
        }
        assertEquals(expectedCounterparty1, actualCounterparty);

    }
    public void testSaveCounterparty() throws SQLException {
        counterpartyDao.save(expectedCounterparty1);
        String sqlQuery = "SELECT * FROM counterparty";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sqlQuery);

        Counterparty actualCounterparty = null;
        if (resultSet.next()) {
            actualCounterparty = new Counterparty(resultSet.getInt("id"),
                    resultSet.getString("name"), resultSet.getString("accountNumber"), resultSet.getLong("amount"));
        }
        assertEquals(expectedCounterparty1, actualCounterparty);
    }

    public void testGetCounterpartyByNumber() throws SQLException {
        String sqlQuery = "insert into counterparty (name, accountnumber, amount) values (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        preparedStatement.setString(1, expectedCounterparty1.getName());
        preparedStatement.setString(2, expectedCounterparty1.getAccountNumber());
        preparedStatement.setLong(3, expectedCounterparty1.getAmount());
        preparedStatement.execute();
        preparedStatement.close();
        assertEquals(expectedCounterparty1, counterpartyDao.getByNumber(expectedCounterparty1.getAccountNumber()));
    }

}
