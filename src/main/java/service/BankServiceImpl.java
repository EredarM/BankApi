package service;

import model.dao.*;
import model.entity.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import supportClass.ResponseEntity;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import java.io.IOException;
import java.io.InputStream;

public class BankServiceImpl implements BankService {
    CardDAO cardDAO;
    DAO<BankAccount> bankAccountDAO;
    CounterpartyDao counterpartyDao;

    public BankServiceImpl() {
        cardDAO = new CardDaoImp();
        bankAccountDAO = new BankAccountDaoImp();
        counterpartyDao = new CounterpartyDaoImpl();
    }

    /**
     * Добавление карты
     *
     * @param inputStream - запрос, содержит номер банковского счёта в байтах
     * @return код состояния
     */
    @Override
    public ResponseEntity createNewCard(InputStream inputStream) {
        try {
            JSONObject jsonObject = getBodyParameters(inputStream);
            String accountNumber = (String) jsonObject.get("accountnumber");
            String newCardNumber = cardNumberGenerator();
            cardDAO.save(new Card(newCardNumber, accountNumber));
            return new ResponseEntity(200, "");
        } catch (SQLException | ParseException | ClassCastException exception) {
            return new ResponseEntity(400, "Bad request");
        }
    }

    /**
     * Получение списка карт аккаунта
     *
     * @param paramString - запрос, содержит номер банковоского счёта
     * @return список карт
     */
    @Override
    public ResponseEntity getCardList(String paramString) {
        try {
            Map<String, String> params = getURLParamToMap(paramString);

            JSONObject jsonObject;
            String accountNumber = params.get("accountnumber");
            List<Card> cardList = cardDAO.getAllCard(accountNumber);
            JSONArray responseJsonArray = new JSONArray();
            for (Card card : cardList) {
                jsonObject = new JSONObject();
                jsonObject.put("id", card.getId());
                jsonObject.put("number", card.getCardNumber());
                responseJsonArray.add(jsonObject);
            }
            return new ResponseEntity(200, responseJsonArray.toString());
        } catch (SQLException exception) {
            return new ResponseEntity(404, "Not found");
        } catch (ClassCastException | NullPointerException exception) {
            return new ResponseEntity(400, "Bad request");
        }
    }

    /**
     * Внесение средств
     *
     * @param inputStream запрос, содежрит номер банквоского счёта или карты и колличество средств в байтах
     * @return код состояния
     */
    @Override
    public ResponseEntity deposit(InputStream inputStream) {
        try {
            JSONObject jsonObject = getBodyParameters(inputStream);
            String indexNumber = (String) jsonObject.get("card");
            Long amount = (Long) jsonObject.get("amount");
            BankAccount bankAccount;

            if (indexNumber == null) {
                indexNumber = (String) jsonObject.get("accountnumber");
                bankAccount = bankAccountDAO.getByNumber(indexNumber);
            } else {
                bankAccount = bankAccountDAO.getByNumber(cardDAO.getByNumber(indexNumber).getAccountNumber());
            }

            bankAccount.setAmount(bankAccount.getAmount() + amount);
            bankAccountDAO.update(bankAccount);
            return new ResponseEntity(200, "");
        } catch (SQLException exception) {
            return new ResponseEntity(404, "Not found");
        } catch (ParseException | ClassCastException | NullPointerException exception) {
            return new ResponseEntity(400, "Bad request");
        }
    }

    /**
     * Получение баланса банковского счёта
     *
     * @param paramString запрос, содержит номер банквоского счёта или карты
     * @return колличество средств на счету
     */
    @Override
    public ResponseEntity getBalance(String paramString) {
        try {
            Map<String, String> params = getURLParamToMap(paramString);
            String indexNumber = params.get("card");
            BankAccount bankAccount;
            if (indexNumber == null) {
                indexNumber = params.get("accountnumber");
                bankAccount = bankAccountDAO.getByNumber(indexNumber);
            } else {
                bankAccount = bankAccountDAO.getByNumber(cardDAO.getByNumber(indexNumber).getAccountNumber());
            }
            return new ResponseEntity(200, String.valueOf(bankAccount.getAmount()));
        } catch (SQLException exception) {
            return new ResponseEntity(404, "Not found");
        } catch (NullPointerException exception) {
            return new ResponseEntity(400, "Bad request");
        }

    }

    @Override
    public ResponseEntity addCounterparty(InputStream inputStream) {
        try {
            JSONObject jsonObject = getBodyParameters(inputStream);
            String name = (String) jsonObject.get("name");
            String accountNumber = (String) jsonObject.get("accountnumber");
            long amount = (long) jsonObject.get("amount");
            Counterparty counterparty = new Counterparty(name, accountNumber, amount);
            counterpartyDao.save(counterparty);
            return new ResponseEntity(200, "");
        } catch (SQLException | ParseException | ClassCastException exception) {
            return new ResponseEntity(400, "Bad request");
        }
    }

    @Override
    public ResponseEntity getCounterParty() {
        try {
            List<Counterparty> counterpartyList = counterpartyDao.getAll();
            JSONObject jsonObject;
            JSONArray jsonArray = new JSONArray();
            for (Counterparty counterparty : counterpartyList) {
                jsonObject = new JSONObject();
                jsonObject.put("id", counterparty.getId());
                jsonObject.put("name", counterparty.getName());
                jsonObject.put("accountnumber", counterparty.getAccountNumber());
                jsonObject.put("amount", counterparty.getAmount());
                jsonArray.add(jsonObject);
            }
            return new ResponseEntity(200, jsonArray.toString());
        } catch (SQLException exception) {
            return new ResponseEntity(404, "Not found");
        } catch (NullPointerException exception) {
            return new ResponseEntity(400, "Bad request");
        }

    }

    @Override
    public ResponseEntity depositToCounterParty(InputStream inputStream) {
        try {
            JSONObject jsonObject = getBodyParameters(inputStream);
            String counterpartyFrom = (String) jsonObject.get("from");
            String counterpartyTo = (String) jsonObject.get("to");
            long amount = (long) jsonObject.get("amount");

            Counterparty counterparty1 = counterpartyDao.getByNumber(counterpartyFrom);
            Counterparty counterparty2 = counterpartyDao.getByNumber(counterpartyTo);

            if (counterparty1 == null || counterparty2 == null) {
                return new ResponseEntity(404, "Not found");
            }

            if (counterparty1.getAmount() < amount || counterparty2.getAmount() < amount) {
                return new ResponseEntity(400, "Bad request");
            }

            counterparty1.setAmount(counterparty1.getAmount() - amount);
            counterparty2.setAmount(counterparty2.getAmount() + amount);

            counterpartyDao.update(counterparty1);
            counterpartyDao.update(counterparty2);

            return new ResponseEntity(200, "");

        } catch (SQLException | ParseException | ClassCastException exception) {
            return new ResponseEntity(400, "Bad request");
        }

    }

    private Map<String, String> getURLParamToMap(String query) {
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            } else {
                result.put(entry[0], "");
            }
        }
        return result;
    }

    private String cardNumberGenerator() {
        Random randomNumGenerator = new Random();
        StringBuilder finalCardNumber = new StringBuilder();
        for (int i = 1; i <= 16; i++) {
            finalCardNumber.append(randomNumGenerator.nextInt(9));
        }
        return finalCardNumber.toString();
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

    private JSONObject getBodyParameters(InputStream inputStream) throws ParseException {
        String jsonString = parseRequestBody(inputStream);
        Object jsonParser = new JSONParser().parse(jsonString);
        JSONObject jsonObject = (JSONObject) jsonParser;
        return jsonObject;
    }
}