import junit.framework.TestCase;
import model.dao.*;
import model.entity.Card;
import org.apache.ibatis.jdbc.ScriptRunner;
import supportClass.ConnectionFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DaoCardTest extends TestCase {
    private CardDAO cardDAO;
    private Connection connection;

    private Card expectedCard;
    private Card expectedCard2;

    @Override
    public void setUp() throws Exception {
        String url = "jdbc:h2:/Users/a19215121/IdeaProjects/BankApi/src/main/resources/testBank;AUTO_SERVER=TRUE";
        String user = "root";
        String password = "testtest";
        connection = ConnectionFactory.getConnection(url, user, password);
        ScriptRunner scriptRunner = new ScriptRunner(connection);
        cardDAO = new CardDaoImp();
        Reader reader = new BufferedReader(new FileReader("src/main/resources/testsDBScripts/createTableCardScript.sql"));
        scriptRunner.runScript(reader);

        expectedCard = new Card(1, "123", "1234");
        expectedCard2 = new Card(2, "87667678", "1234");
    }

    public void testCardSave() throws Exception {
        cardDAO.save(expectedCard);

        String query = "SELECT * FROM card";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        Card actualCard = null;
        if (resultSet.next()) {
            actualCard = new Card(resultSet.getInt("Id"), resultSet.getString("cardNumber"), resultSet.getString("accountNumber"));
        }
        assertEquals(expectedCard, actualCard);

    }

    public void testCardUpdate() throws Exception {
        String query = "insert into Card (cardNumber, accountNumber) values (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, expectedCard.getCardNumber());
        preparedStatement.setString(2, expectedCard.getAccountNumber());
        preparedStatement.execute();

        expectedCard.setCardNumber("11111111");
        cardDAO.update(expectedCard);

        query = "SELECT * FROM card";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        Card actualCard = null;
        if (resultSet.next()) {
            actualCard = new Card(resultSet.getInt("Id"), resultSet.getString("cardNumber"), resultSet.getString("accountNumber"));
        }
        assertEquals(expectedCard, actualCard);
    }

    public void testCardGetByCardNumber() throws Exception {
        String query = "insert into Card (cardNumber, accountNumber) values (?, ?);";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, expectedCard.getCardNumber());
        preparedStatement.setString(2, expectedCard.getAccountNumber());
        preparedStatement.execute();

        assertEquals(expectedCard, cardDAO.getByNumber(expectedCard.getCardNumber()));
    }


    public void testCardGetAllCard() throws Exception {
        String query = "insert into Card (cardNumber, accountNumber) values (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, expectedCard.getCardNumber());
        preparedStatement.setString(2, expectedCard.getAccountNumber());
        preparedStatement.execute();

        preparedStatement.setString(1, expectedCard2.getCardNumber());
        preparedStatement.setString(2, expectedCard2.getAccountNumber());
        preparedStatement.execute();

        List<Card> cardList = new ArrayList<>();
        cardList.add(expectedCard);
        cardList.add(expectedCard2);

        assertEquals(cardList, cardDAO.getAllCard(expectedCard.getAccountNumber()));
    }
}
