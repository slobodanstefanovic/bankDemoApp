package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.DBController;
import model.Transaction;

/**
 * Servlet implementation class ReturnReportsFromDB
 */
@WebServlet(name = "ReturnReportsFromDB", urlPatterns = { "/ReturnReportsFromDB" })
public class ReturnReportsFromDB extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String account;
	private static List<Transaction> filteredListOfTransactions;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/plain");
		account = request.getParameter("name");
//		System.out.println(account);
//		System.out.println("Dosao do servleta sa fronta");
		DBController.returnTransactionsFromDB();
		filterTransactionByAccountName(account);
		response.getWriter().print(formatAccountList());
	}

	public void filterTransactionByAccountName(String accountName) {
		filteredListOfTransactions = new ArrayList<Transaction>();
		for (int i = 0; i < DBController.transactions.size(); i++) {
			if (accountName.equalsIgnoreCase(DBController.transactions.get(i).getAccountFrom().getName())
					|| accountName.equalsIgnoreCase(DBController.transactions.get(i).getAccountTo().getName())) {
				filteredListOfTransactions.add(DBController.transactions.get(i));

			}
		}
	}

	public String formatAccountList() {
		StringBuilder builder = new StringBuilder();
		for (Transaction transaction : filteredListOfTransactions) {
			builder.append("<p>" + transaction + " </p> <br>");
		}

		return builder.toString();

	}

}
