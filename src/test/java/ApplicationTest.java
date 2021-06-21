import com.sun.net.httpserver.HttpServer;
import junit.framework.TestCase;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import supportClass.ConnectionFactory;
import supportClass.CreateServer;


import java.io.*;


public class ApplicationTest extends TestCase {
    private int expectedStatusCode = 200;
    HttpServer server = null;

    @Override
    public void setUp() {
        String url = "jdbc:h2:/Users/a19215121/IdeaProjects/BankApi/src/main/resources/testBank;AUTO_SERVER=TRUE";
        String user = "root";
        String password = "testtest";
        ConnectionFactory.getConnection(url, user, password);
        if (server == null) {
            server = CreateServer.getServer();
        }
    }

    public void testShowCardList() throws IOException {
        HttpGet httpGet = new HttpGet("http://localhost:8000/showCardList?accountnumber=1234");
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(httpGet);
        String expectedBodyJson = "[{\"number\":\"32323232\",\"id\":1}]";
        assertEquals(expectedBodyJson, parseRequestBody(httpResponse.getEntity().getContent()));
    }

    public void testCreateCard() throws IOException {
        HttpPost httpPost = new HttpPost("http://localhost:8000/createNewCard");
        String actualBodyJsonCreateCard = "{\"accountnumber\":\"1234\"}";
        HttpEntity httpEntity = new StringEntity(actualBodyJsonCreateCard, ContentType.APPLICATION_JSON);
        httpPost.setEntity(httpEntity);
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(httpPost);
        assertEquals(expectedStatusCode, httpResponse.getStatusLine().getStatusCode());
    }

    public void testDeposit() throws IOException {
        HttpPost httpPost = new HttpPost("http://localhost:8000/deposit");
        String actualJsonBody = "{\"accountnumber\": \"1234\",\"amount\": 1000}";
        HttpEntity httpEntity = new StringEntity(actualJsonBody, ContentType.APPLICATION_JSON);
        httpPost.setEntity(httpEntity);
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(httpPost);
        assertEquals(expectedStatusCode, httpResponse.getStatusLine().getStatusCode());
    }

    public void testShowBalanceByAccountNumber() throws IOException {
        HttpGet httpGet = new HttpGet("http://localhost:8000/showBalance?accountnumber=5678");
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(httpGet);
        assertEquals("2222", parseRequestBody(httpResponse.getEntity().getContent()));
    }

    public void testShowBalanceByCard() throws IOException {
        HttpGet httpGet = new HttpGet("http://localhost:8000/showBalance?card=87667678");
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(httpGet);
        assertEquals("2222", parseRequestBody(httpResponse.getEntity().getContent()));
    }

    public void testAddCounterparty() throws IOException {
        HttpPost httpPost = new HttpPost("http://localhost:8000/createNewCounterparty");
        String actualJsonBody = "{\"name\": \"third\",\"accountnumber\": \"765433456\",\"amount\": 65432}";
        HttpEntity httpEntity = new StringEntity(actualJsonBody, ContentType.APPLICATION_JSON);
        httpPost.setEntity(httpEntity);
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(httpPost);
        assertEquals(expectedStatusCode, httpResponse.getStatusLine().getStatusCode());
    }

    public void testShowCounterparty() throws IOException {
        String responseJson = "[{\"amount\":50000,\"accountnumber\":\"33333\",\"name\":\"first\",\"id\":1}," +
                "{\"amount\":80000,\"accountnumber\":\"55555\",\"name\":\"second\",\"id\":2}]";
        HttpGet httpGet = new HttpGet("http://localhost:8000/showAllCounterparty");
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(httpGet);
        assertEquals(responseJson, parseRequestBody(httpResponse.getEntity().getContent()));
    }

    public void testDepositToCounterparty() throws IOException {
        HttpPost httpPost = new HttpPost("http://localhost:8000/depositToCounterparty");
        String actualJsonBody = "{\"from\": \"55555\",\"to\": \"33333\",\"amount\": 20000}";
        HttpEntity httpEntity = new StringEntity(actualJsonBody, ContentType.APPLICATION_JSON);
        httpPost.setEntity(httpEntity);
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(httpPost);
        assertEquals(expectedStatusCode, httpResponse.getStatusLine().getStatusCode());
    }

    private String parseRequestBody(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();

        int b;
        try {
            while ((b = inputStream.read()) != -1)
                stringBuilder.append((char) b);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return stringBuilder.toString();
    }
}
