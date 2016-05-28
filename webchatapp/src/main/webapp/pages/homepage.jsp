<%@ page contentType="text/html;charset=UTF-8" language="java"  isELIgnored="false" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Chat</title>
    <link rel="stylesheet" href="css+js/style.css">
    <script src="css+js/scripts.js"></script>
</head>
<body>
<form class="upper-part" action="${pageContext.request.contextPath}/chat/logout" method="get">
    <h1 class="name-chat">Chat</h1>
    <input class="changeUsername" type="submit" value="Logout">
</form>
<div class="currentUser">
    Current user:
    <span class="userHolder">${pageContext.request.getParameter('login')}</span>
</div>
<div class="main-part">
    <div class="chat-messages">
    </div>
    <textarea name="msg-area" id="msg-input" placeholder="Type message"></textarea>
    <button id="send">Send</button>
    <div class="need"></div>
</div>
<div class="ServerError"></div>
</body>
</html>
