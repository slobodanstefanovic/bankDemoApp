package model;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import config.LoggerConfig;
import database.DBController;
import util.DataReader;

public class Transaction extends AbstractDataModel {

    private static final int NUMBER_OF_FIELDS = 3;
    private static final Logger LOGGER = LoggerConfig.getLoggerInstance();
    private static final String CLASS_NAME = "Transaction";

    private double amount;
    private boolean status;
    private Account accountFrom;
    private Account accountTo;
//    private String fromAccount;
//    private String toAccount;

    public Transaction(Account accountFrom, Account accountTo, double amount) {
        this.amount = amount;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
    }

    public Transaction() {
        this.amount = 0;
        accountFrom = new Account();
        accountTo = new Account();
    }

    public Account getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(Account accountFrom) {
        this.accountFrom = accountFrom;
    }

    public Account getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(Account accountTo) {
        this.accountTo = accountTo;
    }

    public double getAmount() {
        return amount;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public Transaction readObjectFromCSVLine(String line) {
        String methodName = "readObjectFromCSVLine(String line)";
        LOGGER.entering(CLASS_NAME, methodName);
        try {
            LOGGER.fine("Reading line");
            String[] csvData = readLine(line);
            if (csvData.length != NUMBER_OF_FIELDS) {
                throw new ArrayIndexOutOfBoundsException();
            }
            LOGGER.fine("Read line");
            String fromAccountCSV = csvData[0];
            String toAccountCSV = csvData[1];
            Double amountCSV = Double.parseDouble(csvData[2]);
            LOGGER.fine("Creating object");

            accountFrom = DBController.getAccountByName(fromAccountCSV);
            accountTo = DBController.getAccountByName(toAccountCSV);

            if (accountFrom != null && accountTo != null) {
                boolean isFromAccountValid = accountFrom.getName().equalsIgnoreCase(fromAccountCSV);
                boolean isToAccountValid = accountTo.getName().equalsIgnoreCase(toAccountCSV);
                if (isFromAccountValid && isToAccountValid) {
                    Transaction transaction = new Transaction(accountFrom, accountTo, amountCSV);
                    LOGGER.log(Level.FINE, "Created object {0}", transaction.toString());
                    LOGGER.exiting(CLASS_NAME, methodName);
                    return transaction;
                }
            }

            
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            LOGGER.severe(e.getMessage());
            LOGGER.exiting(CLASS_NAME, methodName);
            return null;
        }
        return null;
    }

    @Override
    public String toString() {
		return accountFrom + " transfered to " + accountTo + " amount: " + amount;
    }
    
    public static List<AbstractDataModel> getDataList(HttpServletRequest request){
        try{
            Transaction transaction = new Transaction();
            String methodName = "getDataList(HttpServletRequest request)";
            LOGGER.entering(CLASS_NAME, methodName);
            LOGGER.info("Reading from file");
            
            String paramName = "transactionsFile";
            Part filePart = request.getPart(paramName);
            
            String fileText = DataReader.readFileText(filePart.getInputStream());
            DataReader.readDataFromCSV(fileText, transaction);
            LOGGER.fine("Read from file");
            LOGGER.exiting(CLASS_NAME, methodName);
            
            return DataReader.returnRightList(transaction);
        } catch (IOException | ServletException ex){
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }
}
