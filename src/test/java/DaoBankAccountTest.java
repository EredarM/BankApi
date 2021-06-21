import junit.framework.TestCase;
import model.dao.BankAccountDaoImp;
import model.dao.DAO;
import model.entity.BankAccount;
import org.apache.ibatis.jdbc.ScriptRunner;
import supportClass.ConnectionFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class DaoBankAccountTest extends TestCase {
    private DAO<BankAccount> bankAccountDAO;
    private Connection connection;
    private BankAccount expectedBankAccount;

    @Override
    public void setUp() throws Exception {
        String url = "jdbc:h2:/Users/a19215121/IdeaProjects/BankApi/src/main/resources/testBank;AUTO_SERVER=TRUE";
        String user = "root";
        String password = "testtest";
        connection = ConnectionFactory.getConnection(url, user, password);
        ScriptRunner scriptRunner = new ScriptRunner(connection);
        bankAccountDAO = new BankAccountDaoImp();
        Reader reader = new BufferedReader(new FileReader("src/main/resources/testsDBScripts/createTableBankAccountScript.sql"));
        scriptRunner.runScript(reader);

        expectedBankAccount = new BankAccount(1, "1234", 1111, 1);
    }

    public void testBankAccountSave() throws Exception {

        bankAccountDAO.save(expectedBankAccount);
        String query = "SELECT * FROM bankAccount";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        BankAccount actualBankAccount = null;
        if (resultSet.next()) {
            actualBankAccount = new BankAccount(resultSet.getInt("Id"),
                    resultSet.getString("accountNumber"), resultSet.getLong("amount"), resultSet.getInt("clientId"));
        }
        assertEquals(expectedBankAccount, actualBankAccount);
    }

    public void testBankAccountUpdate() throws Exception {
        String query = "insert into BankAccount(clientId, accountNumber, amount) values (?, ?, ?);";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, expectedBankAccount.getId());
        preparedStatement.setString(2, expectedBankAccount.getAccountNumber());
        preparedStatement.setLong(3, expectedBankAccount.getAmount());
        preparedStatement.execute();

        expectedBankAccount.setAmount(1000000);
        bankAccountDAO.update(expectedBankAccount);

        query = "SELECT * FROM bankAccount";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        BankAccount actualBankAccount = null;
        if (resultSet.next()) {
            actualBankAccount = new BankAccount(resultSet.getInt("Id"),
                    resultSet.getString("accountNumber"), resultSet.getLong("amount"), resultSet.getInt("clientId"));
        }
        assertEquals(expectedBankAccount, actualBankAccount);
    }

    public void testBankAccountGetByNumber() throws Exception {
        String query = "insert into BankAccount(clientId, accountNumber, amount) values (?, ?, ?);";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, expectedBankAccount.getId());
        preparedStatement.setString(2, expectedBankAccount.getAccountNumber());
        preparedStatement.setLong(3, expectedBankAccount.getAmount());
        preparedStatement.execute();

        assertEquals(expectedBankAccount, bankAccountDAO.getByNumber(expectedBankAccount.getAccountNumber()));
    }
}
