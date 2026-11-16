<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Tạo tài khoản mới</title>
</head>
<body>
    <form action="${pageContext.request.contextPath}/register" method="post">
        <h2>Tạo tài khoản mới</h2>

        <c:if test="${alert != null}">
            <h3 style="color: red;">${alert}</h3>
        </c:if>

        <div style="margin-bottom: 10px;">
            <input type="text" placeholder="Tài khoản (Username)" name="username" required />
        </div>
        <div style="margin-bottom: 10px;">
            <input type="text" placeholder="Họ tên" name="fullname" required />
        </div>
        <div style="margin-bottom: 10px;">
            <input type="email" placeholder="Nhập Email" name="email" required />
        </div>
        <div style="margin-bottom: 10px;">
            <input type="text" placeholder="Số điện thoại" name="phone" required />
        </div>
        <div style="margin-bottom: 10px;">
            <input type="password" placeholder="Mật khẩu" name="password" required />
        </div>
        <button type="submit">Tạo tài khoản</button>
        <p>Nếu bạn đã có tài khoản? <a href="${pageContext.request.contextPath}/login">Đăng nhập</a></p>
    </form>
</body>
</html>