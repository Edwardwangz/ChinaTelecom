<%--
  Created by IntelliJ IDEA.
  User: wangz
  Date: 2019/8/22
  Time: 15:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>通话记录</title>
    <link rel="stylesheet" type="text/css" href="../css/my.css">
</head>
<body>
<table id="t1" border="1px" class="t-1">
    <tr>
        <td>主叫</td>
        <td>被叫</td>
        <td>通话时间</td>
        <td>通话时长</td>
    </tr>
    <c:forEach items="${callLogs}" var="log">
        <tr>
            <td><c:out value="${log.calling}"/></td>
            <td><c:out value="${log.called}"/></td>
            <td><c:out value="${log.callTime}"/></td>
            <td><c:out value="${log.duration}"/></td>
        </tr>
    </c:forEach>
    <tr>
        <td colspan="5" style="text-align: right">
        </td>
    </tr>
</table>
</body>
</html>