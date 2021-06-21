package controllers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.BankService;
import service.BankServiceImpl;
import supportClass.ResponseEntity;

import java.io.IOException;
import java.io.OutputStream;

public class ShowBalance implements HttpHandler {
    private BankService bankService;

    public ShowBalance() {
        bankService = new BankServiceImpl();
    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        ResponseEntity response;
        if (t.getRequestMethod().equals("GET")) {
            response = bankService.getBalance(t.getRequestURI().getQuery());
        } else {
            response = new ResponseEntity(405, "Method Not Allowed");
        }
        t.sendResponseHeaders(response.getStatus(), response.getMessage().getBytes().length);
        OutputStream os = t.getResponseBody();
        os.write(response.getMessage().getBytes());
        os.close();
    }
}