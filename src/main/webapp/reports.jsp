<%@page import="model.Transaction"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="model.Account"%>
<%@ page import="java.util.List"%>
<%@ page import="database.DBController"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JSP Page</title>
<link rel="stylesheet" type="text/css" href="css/index.css" />
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script src="js/index.js"></script>
</head>
<body>
	<header>
		<nav>
			<ul>
				<li><a href="/Bank/index.jsp">Home</a></li>
				<li><a href="/Bank/upload.jsp">Upload</a></li>
				<li><a href="/Bank/">Transactions</a></li>
				<li><a href="/Bank/reports.jsp">Reports</a></li>
			</ul>
		</nav>
		<a id="login" href="#"><button>Login</button></a>
	</header>

	<%
	List<Account> accounts = DBController.getAllAccounts();
	%>

	<form id="accountReport" onsubmit="return getReport()">

		Choose report for: <select id="accountForReport">

			<option value="none" selected disabled hidden=false>Select
				report</option>
			<%
			for (int i = 0; i < accounts.size(); i++) {
			%>
			<option value="<%=accounts.get(i).getName()%>"><%=accounts.get(i).getName()%></option>
			<%}%>
		</select> <br> <br /> <input type="submit" value="Get Report"
			id="getReportBtn">
	</form>

	<div id="transactionsFromSelectedName"
		style="color: white; text-align: center;"></div>
</body>
</html>
