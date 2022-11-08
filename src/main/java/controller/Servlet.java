package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import database.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.*;

@WebServlet(name = "PostTransaction", urlPatterns = {"/PostTransaction"})
public class Servlet extends HttpServlet {

    private static final String TRANSACTION_RECORDED_MESSAGE = "Transaction has been recorded.";
    private static String nameAccountTo;
    private static String nameAccountFrom;
    private static double amount;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {

        response.setContentType("text/plain");
        nameAccountTo = request.getParameter("receiver");
        nameAccountFrom = request.getParameter("sender");
        amount = Double.parseDouble(request.getParameter("amount"));

        Account fromAccount = DBController.getAccountByName(nameAccountFrom);
        Account toAccount = DBController.getAccountByName(nameAccountTo);

        Transaction transaction = new Transaction(fromAccount, toAccount, amount);
        transaction.setStatus(DBController.isTransactionValid(transaction));

        submitTransaction(transaction);

        try {
            response.getWriter().println(TRANSACTION_RECORDED_MESSAGE);
        } catch (IOException ex) {
            Logger.getLogger(Servlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void submitTransaction(Transaction transaction) {

        if (transaction.isStatus()) {
            calculateBalance(transaction);
            DBController.updateAccount(transaction.getAccountFrom());
            DBController.updateAccount(transaction.getAccountTo());
        }
        DBController.submitTransaction(transaction);
    }

    private void calculateBalance(Transaction transaction) {
        transaction.getAccountFrom().setBalance(transaction.getAccountFrom().getBalance() - amount);
        transaction.getAccountTo().setBalance(transaction.getAccountTo().getBalance() + amount);
    }

}
