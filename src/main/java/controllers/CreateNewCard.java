package controllers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.BankService;
import service.BankServiceImpl;
import supportClass.ResponseEntity;

import java.io.IOException;
import java.io.OutputStream;

public class CreateNewCard implements HttpHandler {
    private BankService bankService;

    public CreateNewCard() {
        bankService = new BankServiceImpl();
    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        ResponseEntity response;
        if (t.getRequestMethod().equals("POST")) {
            response = bankService.createNewCard(t.getRequestBody());
        } else {
            response = new ResponseEntity(405, "Method Not Allowed");
        }
        t.sendResponseHeaders(response.getStatus(), response.getMessage().getBytes().length);
        OutputStream os = t.getResponseBody();
        os.write(response.getMessage().getBytes());
        os.close();
    }
}
