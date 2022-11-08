package database;

import static database.DatabaseConnector.closeConnection;
import static database.DatabaseConnector.connection;
import static database.DatabaseConnector.openConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import config.LoggerConfig;
import model.Account;
import model.Transaction;

public class DBController {

	private static final Logger LOGGER = LoggerConfig.getLoggerInstance();
	private static final String CLASS_NAME = "DBController";
	public static List<Transaction> transactions;

	public static boolean isTransactionValid(Transaction transaction) {
		final String METHODNAME = "isTransactionValid(Transaction transaction)";
		final String GET_BALANCE_QUERY = "SELECT Balance FROM account WHERE Name='"
				+ transaction.getAccountFrom().getName() + "';";

		LOGGER.entering(CLASS_NAME, METHODNAME);

		Statement statement = null;
		try {

			LOGGER.fine("Creating statement");
			statement = connection.createStatement();

			LOGGER.info(MessageFormat.format("Executing query: {0}", GET_BALANCE_QUERY));
			ResultSet rs = statement.executeQuery(GET_BALANCE_QUERY);
			while (rs.next()) {
				int balance = rs.getInt("Balance");
				return balance >= transaction.getAmount();
			}
		} catch (SQLException ex) {
			LOGGER.log(Level.SEVERE, null, ex);
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException ex) {
					Logger.getLogger(DBController.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
		LOGGER.exiting(CLASS_NAME, METHODNAME);

		return false;
	}

	public static void updateAccount(Account account) {
		final String methodName = "updateAccountData(Transaction transaction)";
		final String UPDATING_ACCOUNT = "UPDATE account SET Balance=" + account.getBalance() + " WHERE Name='"
				+ account.getName() + "';";

		LOGGER.entering(CLASS_NAME, methodName);
		Statement statement = null;

		try {
			LOGGER.fine("Creating statement");
			statement = connection.createStatement();

			LOGGER.info(MessageFormat.format("Executing query: {0}", UPDATING_ACCOUNT));
			statement.execute(UPDATING_ACCOUNT);

		} catch (SQLException ex) {
			LOGGER.log(Level.SEVERE, null, ex);
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException ex) {
					Logger.getLogger(DBController.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
		LOGGER.exiting(CLASS_NAME, methodName);
	}

	public static Account getAccountByName(String name) {
		final String METHODNAME = "getAccountIdByName(String name)";
		final String SELECT_ID_BY_ACCOUNT_NAME_QUERY = "SELECT * FROM account WHERE Name='" + name + "';";
		Account account = null;
		openConnection();

		LOGGER.entering(CLASS_NAME, METHODNAME);
		Statement statement = null;
		try {
			LOGGER.fine("Creating statement");
			statement = connection.createStatement();

			LOGGER.info(MessageFormat.format("Executing query: {0}", SELECT_ID_BY_ACCOUNT_NAME_QUERY));
			ResultSet rs = statement.executeQuery(SELECT_ID_BY_ACCOUNT_NAME_QUERY);

			while (rs.next()) {

				if (rs.getString("Name").equalsIgnoreCase(name)) {
					account = new Account();
					account.setName(name);
					account.setId(rs.getLong("ID"));
					account.setBalance(rs.getDouble("Balance"));
				}
			}
		} catch (SQLException ex) {
			LOGGER.log(Level.SEVERE, null, ex);
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException ex) {
					Logger.getLogger(DBController.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
		return account;
	}

	public static void submitTransaction(Transaction transaction) {
		final String METHODNAME = "submitTransaction(Transaction transaction)";
		final String INSERTING_TRANSACTION_QUERY = "INSERT INTO transaction(From_ID,To_ID,Amount,Status) VALUES ("
				+ transaction.getAccountFrom().getId() + "," + transaction.getAccountTo().getId() + ","
				+ transaction.getAmount() + "," + transaction.isStatus() + ");";

		LOGGER.entering(CLASS_NAME, METHODNAME);
		Statement statement = null;
		try {

			LOGGER.fine("Creating statement");
			statement = connection.createStatement();

			LOGGER.info(MessageFormat.format("Executing query: {0}", INSERTING_TRANSACTION_QUERY));
			statement.execute(INSERTING_TRANSACTION_QUERY);

		} catch (SQLException ex) {
			LOGGER.log(Level.SEVERE, null, ex);
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException ex) {
					Logger.getLogger(DBController.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
			closeConnection();
		}
		LOGGER.exiting(CLASS_NAME, METHODNAME);
	}

	public static void insertAccounts(List<Account> accounts) {
		String methodName = "insertAccounts(List<AbstractDataModel> accounts)";
		LOGGER.entering(CLASS_NAME, methodName);
		openConnection();
		for (Account account : accounts) {
			double getBalance = account.getBalance();
			String getName = account.getName();
			final String INSERT_INTO_ACCOUNT_QUERY = MessageFormat
					.format("insert into bank.account(Name, Balance) values(''{0}'',{1});", getName, getBalance);
			try (Statement stmt = DatabaseConnector.connection.createStatement();) {
				LOGGER.info(MessageFormat.format("Executing query: {0}", INSERT_INTO_ACCOUNT_QUERY));
				stmt.execute(INSERT_INTO_ACCOUNT_QUERY);
			} catch (SQLException ex) {
				LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
			}
		}
	}

	public static void insertTransactions(List<Transaction> transactions) {
		final String methodName = "insertTransactions(List<AbstractDataModel> transactions)";
		LOGGER.entering(CLASS_NAME, methodName);
		for (Transaction transaction : transactions) {
			boolean isTransactionValid = isTransactionValid(transaction);
			double amount = transaction.getAmount();
			long getFromId = transaction.getAccountFrom().getId();
			long getToId = transaction.getAccountTo().getId();
			final String INSERT_INTO_TRANSACTIONS_QUERY = MessageFormat.format(
					"insert into bank.transaction(From_ID, To_ID, Amount, Status) values({0},{1},{2},{3});", getFromId,
					getToId, amount, isTransactionValid);
			try (Statement stmt = DatabaseConnector.connection.createStatement();) {
				LOGGER.info(MessageFormat.format("Executing query: {0}", INSERT_INTO_TRANSACTIONS_QUERY));
				stmt.execute(INSERT_INTO_TRANSACTIONS_QUERY);
			} catch (SQLException ex) {
				LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
			}
		}
		closeConnection();
	}

	public static List<Account> getAllAccounts() {
		final String methodName = "getAllAccounts()";
		final String SELECT_ALL_ACCOUNTS = "SELECT * FROM bank.account;";
		final String ID_LABEL = "ID";
		final String NAME_LABEL = "Name";
		final String BALANCE_LABEL = "Balance";

		Account account = null;
		List<Account> accounts = new ArrayList<Account>();
		LOGGER.entering(CLASS_NAME, methodName);
		openConnection();

		try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(SELECT_ALL_ACCOUNTS)) {
			LOGGER.fine("Reading from database");
			while (rs.next()) {
				account = new Account();
				account.setId(rs.getInt(ID_LABEL));
				account.setName(rs.getString(NAME_LABEL));
				account.setBalance(rs.getDouble(BALANCE_LABEL));
				accounts.add(account);
				LOGGER.fine("New line was read");
			}

			LOGGER.fine("Read from database");
			LOGGER.exiting(CLASS_NAME, methodName);
			return accounts;
		} catch (SQLException ex) {
			LOGGER.log(Level.SEVERE, null, ex);
		}
		return null;
	}// Vrati sve transakcije iz baze podataka

	public static void returnTransactionsFromDB() {

		openConnection();
		String sql = "select ac.id as FROM_ID, ac.name as FROM_NAME,"
				+ "ac.balance as FROM_BALANCE, t.amount, t.status, ac2.id as TO_ID, ac2.name as TO_NAME, ac2.balance as TO_BALANCE "
				+ "from transaction t " + "JOIN account ac on ac.id = t.From_id "
				+ "JOIN account ac2 on ac2.id = t.To_id;";
		PreparedStatement preparedStatement;
		try {
			DBController.transactions = new ArrayList<Transaction>();
			preparedStatement = connection.prepareStatement(sql);
			ResultSet resultSet;
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Transaction transaction = new Transaction();
				Account accountTo = new Account();
				Account accountFrom = new Account();
				accountFrom.setId(resultSet.getLong("from_Id"));
				accountTo.setId(resultSet.getLong("to_Id"));
				transaction.setAccountFrom(accountFrom);
				transaction.setAccountTo(accountTo);
				transaction.setAmount(resultSet.getDouble("amount"));
				transaction.setStatus(resultSet.getBoolean("status"));
				transaction.getAccountFrom().setName(resultSet.getString("FROM_NAME"));
				transaction.getAccountTo().setName(resultSet.getString("TO_NAME"));
				transactions.add(transaction);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

//		return transactions;
	}
}
