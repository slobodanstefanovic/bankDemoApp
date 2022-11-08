/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Account;
import util.DataReader;

@WebServlet(name = "ValidationServlet", urlPatterns = {"/ValidationServlet"})

public class ValidationServlet extends HttpServlet {

    private static final String AMOUNT_VALIDATION_MESSAGE_TRUE = "Transaction validation succeeded.";
    private static final String AMOUNT_VALIDATION_MESSAGE_FALSE = "Transaction validation failed: insufficient funds.";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain");
        String amount = request.getParameter("amount");
        String sender = request.getParameter("sender");

        String result = checkSendingAmount(Double.parseDouble(amount), sender); // parseDouble is safe - data checked on FE

        response.getWriter().println(result);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private String checkSendingAmount(double amount, String sender) {

        List<Account> accounts = DataReader.getAccounts();
        for (Account a : accounts) {
            if (a.getName().equalsIgnoreCase(sender)) {
                if (a.getBalance() >= amount) {
                    return AMOUNT_VALIDATION_MESSAGE_TRUE;
                }
            }
        }
        return AMOUNT_VALIDATION_MESSAGE_FALSE;

    }

}
