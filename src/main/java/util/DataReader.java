package util;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import config.LoggerConfig;
import model.AbstractDataModel;
import model.Account;
import model.Transaction;
import config.LoggerConfig;
import java.io.IOException;
import java.io.InputStream;

public class DataReader {

    private static final String SPLIT_BY = "\n";
    private static final Logger LOGGER = LoggerConfig.getLoggerInstance();
    private static final String CLASS_NAME = "DataReader";

    private static List<Account> accounts = new ArrayList<Account>();
    private static List<Transaction> transactions = new ArrayList<Transaction>();

    private DataReader() {
    }

    public static void readDataFromCSV(String fileText, AbstractDataModel data) {
        String methodName = "readDataFromCSV()";
        LOGGER.entering(CLASS_NAME, methodName);
        String[] fileLines = fileText.split(SPLIT_BY);
        List<AbstractDataModel> list = new ArrayList<AbstractDataModel>();
        LOGGER.info("Reading lines...");
        for (String fileLine : fileLines) {
            AbstractDataModel readData = (AbstractDataModel) data.readObjectFromCSVLine(fileLine);
            if (readData != null) {
                list.add(readData);
            } else {
                LOGGER.severe("Line couldn't be read");
            }
        }
        LOGGER.fine("Read lines");
        assignList(list, data);
        LOGGER.exiting(CLASS_NAME, methodName);
    }

    /**
     *
     * @param list
     * @param data We assign loaded list to one of the existing lists, depending
     * on a type of a loaded list We need to send variable data in case loaded
     * list doesn't have any elements
     */
    private static void assignList(List<AbstractDataModel> list, AbstractDataModel data) {
        String methodName = "assignList()";
        try {
            LOGGER.entering(CLASS_NAME, methodName);
            if (data instanceof Account) {
                accounts = (List<Account>) (List<?>) list;
                LOGGER.info("List is assigned to accounts list");
            } else if (data instanceof Transaction) {
                transactions = (List<Transaction>) (List<?>) list;
                LOGGER.info("List is assigned to transactions list");
            }
            LOGGER.exiting(CLASS_NAME, methodName);
        } catch (Exception ex) {
            LOGGER.logp(Level.WARNING, CLASS_NAME, methodName, ex.getLocalizedMessage());
        }
    }

    public static List<Account> getAccounts() {
        return accounts;
    }

    public static List<Transaction> getTransactions() {
        return transactions;
    }

    public static String readFileText(InputStream inputStream) {
        try {
            String methodName = "readFileText()";
            LOGGER.entering(CLASS_NAME, methodName);
            byte[] fileBytes = inputStream.readAllBytes();
            String fileText = "";
            LOGGER.info("Reading from input stream");
            for (int i = 3; i < fileBytes.length; i++) {
                fileText += (char) fileBytes[i];
            }
            LOGGER.info("Read from input stream");
            LOGGER.exiting(CLASS_NAME, methodName);
            return fileText;
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    public static List<AbstractDataModel> returnRightList(AbstractDataModel t) {
        if (t instanceof Account) {
            return (List<AbstractDataModel>) (List<?>) DataReader.getAccounts();
        } else if (t instanceof Transaction) {
            return (List<AbstractDataModel>) (List<?>) DataReader.getTransactions();
        }
        return null;
    }

}
