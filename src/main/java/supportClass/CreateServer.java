package supportClass;

import com.sun.net.httpserver.HttpServer;
import controllers.*;
import org.apache.ibatis.javassist.ClassPath;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;

public class CreateServer {
    private static HttpServer server = null;

    public static HttpServer getServer() {
        if (server == null) {
            createServer();
        }
        return server;
    }

    private static void createServer() {
        if (server == null) {
            try {
                server = HttpServer.create(new InetSocketAddress(8000), 0);
                addAllConfig(server);
                server.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void addAllConfig(HttpServer server) throws FileNotFoundException {
        Connection connection = ConnectionFactory.getConnection();
        ScriptRunner scriptRunner = new ScriptRunner(connection);

        Reader reader = new BufferedReader(new InputStreamReader(CreateServer.class.getClassLoader().getResourceAsStream("startingScript.sql")));
        scriptRunner.runScript(reader);


        server.createContext("/createNewCard", new CreateNewCard());
        server.createContext("/showCardList", new ShowCardList());
        server.createContext("/deposit", new Deposit());
        server.createContext("/showBalance", new ShowBalance());
        server.createContext("/createNewCounterparty", new AddCounterparty());
        server.createContext("/showAllCounterparty", new ShowCounterparty());
        server.createContext("/depositToCounterparty", new DepositToCounterparty());

        server.setExecutor(null);
    }


}
