package model;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import config.LoggerConfig;
import util.DataReader;

public class Account extends AbstractDataModel {

    private static final int NUMBER_OF_FIELDS = 2;
    private static final Logger LOGGER = LoggerConfig.getLoggerInstance();
    private static final String CLASS_NAME = "Account";

    private long id;
    private String name;
    private double balance;

    public Account(String name) {
        this.name = name;
        this.balance = 0;
        id = 0;
    }

    public Account(long id, String name, double balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    public Account(String name, Double balance) {
        this.id = 0;
        this.name = name;
        this.balance = balance;
    }

    public Account() {
        this.id = 0;
        this.name = null;
        this.balance = 0;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name){
        this.name=name;
    }

    public double getBalance() {
        return balance;
    }

    @Override
    public Account readObjectFromCSVLine(String line) {
        String methodName = "readObjectFromCSVLine()";
        LOGGER.entering(CLASS_NAME, methodName);
        try {
            LOGGER.fine("Reading line");
            String[] csvData = readLine(line);
            if (csvData.length != NUMBER_OF_FIELDS) {
                throw new ArrayIndexOutOfBoundsException();
            }
            LOGGER.fine("Read line");
            String nameCSV = csvData[0];
            Double balanceCSV = Double.parseDouble(csvData[1]);
            LOGGER.fine("Creating object");
            Account account = new Account(nameCSV, balanceCSV);
            LOGGER.log(Level.FINE, "Created object {0}", account.toString());
            LOGGER.exiting(CLASS_NAME, methodName);
            return account;
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            LOGGER.severe(e.getMessage());
            LOGGER.exiting(CLASS_NAME, methodName);
            return null;
        }
    }

    @Override
    public String toString() {
		return name;
    }
    
    public static List<AbstractDataModel> getDataList(HttpServletRequest request){
        try{
            Account account = new Account();
            String methodName = "getDataList(HttpServletRequest request)";
            LOGGER.entering(CLASS_NAME, methodName);
            LOGGER.info("Reading from file");
            
            String paramName = "accountsFile";
            Part filePart = request.getPart(paramName);
            
            String fileText = DataReader.readFileText(filePart.getInputStream());
            DataReader.readDataFromCSV(fileText, account);
            LOGGER.fine("Read from file");
            LOGGER.exiting(CLASS_NAME, methodName);
            
            return DataReader.returnRightList(account);
        } catch (IOException | ServletException ex){
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

}
