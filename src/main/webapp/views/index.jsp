<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Trang chủ</title>
</head>
<body>
    <c:choose>
        <c:when test="${sessionScope.account == null}">
            <div>
                <a href="${pageContext.request.contextPath}/login">Đăng nhập</a> | 
                <a href="${pageContext.request.contextPath}/register">Đăng ký</a>
            </div>
        </c:when>
        <c:otherwise>
            <div>
                Xin chào: <b>${sessionScope.account.fullName}</b> | 
                <a href="${pageContext.request.contextPath}/logout">Đăng Xuất</a>
            </div>
        </c:otherwise>
    </c:choose>
    <hr/>
    <h2>Trang chủ hệ thống</h2>
    
    <!-- Nút bấm chuyển thẳng sang trang CRUD Category -->
    <p>
        <a href="${pageContext.request.contextPath}/admin/category/list" 
           style="padding: 8px 15px; background: #007bff; color: white; text-decoration: none; border-radius: 4px;">
            Vào trang Quản lý Danh mục (Category CRUD)
        </a>
    </p>
</body>
</html>