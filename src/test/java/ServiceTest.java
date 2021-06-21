import junit.framework.TestCase;
import model.dao.BankAccountDaoImp;
import org.apache.ibatis.jdbc.ScriptRunner;
import service.BankService;
import service.BankServiceImpl;
import supportClass.ConnectionFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;

public class ServiceTest extends TestCase {
    private BankService bankService;
    private int expectedStatusCode = 200;

    @Override
    public void setUp() throws Exception {
        String user = "root";
        String url = "jdbc:h2:/Users/a19215121/IdeaProjects/BankApi/src/main/resources/testBank;AUTO_SERVER=TRUE";
        String password = "testtest";
        Connection connection = ConnectionFactory.getConnection(url, user, password);
        ScriptRunner scriptRunner = new ScriptRunner(connection);

        Reader reader = new BufferedReader(new FileReader("src/main/resources/startingScript.sql"));
        scriptRunner.runScript(reader);
        bankService = new BankServiceImpl();
    }

    public void testGetCardList() {
        String requestParam = "accountnumber=1234";
        String responseJson = "[{\"number\":\"32323232\",\"id\":1}]";
        assertEquals(responseJson, bankService.getCardList(requestParam).getMessage());
    }

    public void testCreateNewCard() {
        String requestJson = "{\"accountnumber\": \"1234\"}";
        assertEquals(expectedStatusCode, bankService.createNewCard(new ByteArrayInputStream(requestJson.getBytes(StandardCharsets.UTF_8))).getStatus());
    }

    public void testDepositByAccountNumber() {
        String requestJson = "{\"accountnumber\": \"1234\",\"amount\": 1000}";
        assertEquals(expectedStatusCode, bankService.deposit(new ByteArrayInputStream(requestJson.getBytes(StandardCharsets.UTF_8))).getStatus());
    }

    public void testDepositByCardNumber() {
        String requestJson = "{\"card\": \"32323232\",\"amount\": 1000}";
        bankService.deposit(new ByteArrayInputStream(requestJson.getBytes(StandardCharsets.UTF_8)));

        assertEquals("2111", bankService.getBalance("card=32323232").getMessage());
    }

    public void testGetBalanceByAccountNumber() {
        String requestParam = "accountnumber=5678";
        assertEquals("2222", bankService.getBalance(requestParam).getMessage());
    }

    public void testGetBalanceByCardNumber() {
        String requestParam = "card=87667678";
        assertEquals("2222", bankService.getBalance(requestParam).getMessage());
    }

    public void testAddCounterparty () {
        String requestJson = "{\"name\": \"third\",\"accountnumber\": \"765433456\",\"amount\": 65432}";
        assertEquals(expectedStatusCode, bankService.addCounterparty(new ByteArrayInputStream(requestJson.getBytes(StandardCharsets.UTF_8))).getStatus());
    }

    public void testGetCounterParty () {
        String responseJson = "[{\"amount\":50000,\"accountnumber\":\"33333\",\"name\":\"first\",\"id\":1}," +
                "{\"amount\":80000,\"accountnumber\":\"55555\",\"name\":\"second\",\"id\":2}]";
        assertEquals(responseJson, bankService.getCounterParty().getMessage());
    }

    public void testDepositToCounterParty () {
        String requestJson = "{\"from\": \"55555\",\"to\": \"33333\",\"amount\": 20000}";
        assertEquals(expectedStatusCode, bankService.depositToCounterParty(new ByteArrayInputStream(requestJson.getBytes(StandardCharsets.UTF_8))).getStatus());
    }
}
