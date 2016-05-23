<%@  taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ page  contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title>Login</title>
    <link href="css+js/login-styles.css" rel="stylesheet">
</head>
<body>
<div class="login-form-container">
    <form action="${pageContext.request.contextPath}/login" method="post">
        <div class="logging">
            <label id="login-label">Username</label>
            <input id="login-field" name="login">
        </div>
        <div class="passdiv">
            <label id="pass-label">Password</label>
            <input id="pass-field" name="pass" type="password">
        </div>
        <div>
            <button id="logButton" type="submit">Login</button>
        </div>
        <c class="error">
            <c:choose>
            <c:when test="${requestScope.errorMsg!=null}"><c:out value="${requestScope.errorMsg}"></c:out></c:when>
                <c:otherwise></c:otherwise>
            </c:choose>
        </div>
    </form></div>
</body>
</html>