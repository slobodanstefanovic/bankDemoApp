<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" type="text/css" href="css/index.css" />
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
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
        <form action="fileUpload" method="post" enctype="multipart/form-data" id="uploads">
            Upload accounts file:
            <input type="file" name="accountsFile" id="accountsFile" class="fileUpload">
            Upload transactions file:
            <input type="file" name="transactionsFile" id="transactionsFile" class="fileUpload">                                    
            <input type="submit">
        </form>     
    </body>
</html>
