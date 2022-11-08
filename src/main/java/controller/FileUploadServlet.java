package controller;

import config.LoggerConfig;
import database.DatabaseConnector;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import model.AbstractDataModel;
import model.Account;
import util.DataReader;
import model.Transaction;
import database.DBController;
import java.util.logging.Level;

@WebServlet(name = "FileUploadServlet", urlPatterns = {"/fileUpload"})
@MultipartConfig
public class FileUploadServlet extends HttpServlet {

    static final Logger LOGGER = LoggerConfig.getLoggerInstance();
    static final String CLASS_NAME = "FileUploadServlet";
    private static List<Account> accounts;
    private static List<Transaction> transactions;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String methodName = "processRequest()";
        LOGGER.entering(CLASS_NAME, methodName);
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet FileUploadServlet</title>");
            out.println("</head>");
            out.println("<body>");
            
            accounts = (List<Account>) (List<?>) Account.getDataList(request);
            DBController.insertAccounts(accounts);
            transactions = (List<Transaction>) (List<?>) Transaction.getDataList(request);
            DBController.insertTransactions(transactions);

            out.println("<h1>List of uploaded accounts: </h1>");
            printList(out, (List<AbstractDataModel>) (List<?>) accounts);
            out.println("<h1>List of uploaded transactions: </h1>");
            printList(out, (List<AbstractDataModel>) (List<?>) transactions);

            out.println("</body>");
            out.println("</html>");
        }
        LOGGER.exiting(CLASS_NAME, methodName);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void printList(PrintWriter out, List<AbstractDataModel> list) {
        String methodName = "printList()";
        LOGGER.entering(CLASS_NAME, methodName);
        if (list.isEmpty()) {
            LOGGER.info("Empty list");
            out.println("File you uploaded wasn't in right format!");
        } else {
            LOGGER.fine("List has elements");
            for (Object element : list) {
                LOGGER.fine("Printing elements");
                out.println(element + "<br>");
            }
        }
        LOGGER.exiting(CLASS_NAME, methodName);
    }

}
