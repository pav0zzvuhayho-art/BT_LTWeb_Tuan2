<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đăng Nhập Vào Hệ Thống</title>
</head>
<body>
    <form action="${pageContext.request.contextPath}/login" method="post">
        <h2>Đăng Nhập Vào Hệ Thống</h2>
        
        <c:if test="${alert != null}">
            <h3 style="color: red;">${alert}</h3>
        </c:if>

        <div style="margin-bottom: 10px;">
            <input type="text" placeholder="Tài khoản" name="username" required />
        </div>
        <div style="margin-bottom: 10px;">
            <input type="password" placeholder="Mật khẩu" name="password" required />
        </div>
        <div style="margin-bottom: 10px;">
            <label><input type="checkbox" name="remember" /> Nhớ tôi</label>
        </div>
        <button type="submit">Đăng nhập</button>
        <p>Nếu bạn chưa có tài khoản trên hệ thống, thì hãy <a href="${pageContext.request.contextPath}/register">Đăng ký</a></p>
    </form>
</body>
</html>