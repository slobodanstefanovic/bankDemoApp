<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ page import="model.Account"%>
<%@ page import="java.util.List"%>
<%@ page import="controller.ValidationServlet"%>
<%@ page import="database.DBController"%>


<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Bank app demo</title>
        <link rel="stylesheet" type="text/css" href="css/index.css" />
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
        <script src="js/index.js" defer></script>
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

        <br />
        <br />

        <%
            List<Account> accounts = DBController.getAllAccounts();
        %>
        <form id="transfer" onsubmit="return validate()">
            <img src="images/bank-logo.png">
            <br />
            <br />
            Account from:
            <select name="sender" id="sender">
                <option value="none" selected disabled hidden>Select an account</option>
                <%
                    for (int i = 0; i < accounts.size(); i++) {
                %>
                <option value="<%=accounts.get(i).getName()%>"><%=accounts.get(i).getName()%></option>
                <%}%>
            </select>
            <br />
            <br />
            Account to:
            <select name="reciever" id="receiver">
                <option value="none" selected disabled hidden>Select an account</option>
                <%
                    for (int i = 0; i < accounts.size(); i++) {
                %>
                <option value="<%=accounts.get(i).getName()%>"><%=accounts.get(i).getName()%></option>
                <%}%>
            </select>
            <br />
            <br />
            Amount:
            <input type="text" name="amount" id="amount">
            <br />
            <br />
            <input type="submit" value="Transfer" id = "submitBtn">
        </form>
   
        <div id = "validation_response" style = "color:white; text-align:center;"> <br /> </div>
    
</body>
</html>