<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Trang chủ - Top 10 Video Mới Nhất</title>
    <style>
        .grid { display: flex; flex-wrap: wrap; gap: 20px; }
        .card { border: 1px solid #ccc; padding: 10px; width: 220px; text-align: center; }
    </style>
</head>
<body>
    <div>
        <c:choose>
            <c:when test="${not empty sessionScope.account}">
                Xin chào: <b>${sessionScope.account.fullName}</b> |
                <a href="${pageContext.request.contextPath}/video">Tất cả Video (Phân trang)</a> |
                <a href="${pageContext.request.contextPath}/admin/videos">Quản lý Video (CRUD)</a> |
                <a href="${pageContext.request.contextPath}/admin/categories">Quản lý Category</a> |
                <a href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/login">Đăng nhập</a> |
                <a href="${pageContext.request.contextPath}/register">Đăng ký</a> |
                <a href="${pageContext.request.contextPath}/forgot-password">Quên mật khẩu</a> |
                <a href="${pageContext.request.contextPath}/video">Danh sách Video</a>
            </c:otherwise>
        </c:choose>
    </div>
    <hr/>

    <h2>Top 10 Video Mới Nhất</h2>
    <div class="grid">
        <c:forEach items="${top10Videos}" var="v">
            <div class="card">
                <c:url value="/image?fname=${v.poster}" var="posterUrl" />
                <img src="${v.poster != null && v.poster.startsWith('http') ? v.poster : posterUrl}" width="200" height="120" alt="${v.title}" />
                <h4>
                    <a href="${pageContext.request.contextPath}/video/detail?id=${v.videoId}">${v.title}</a>
                </h4>
                <p>Lượt xem: ${v.views} | Đăng bởi: ${v.user != null ? v.user.fullName : 'Admin'}</p>
            </div>
        </c:forEach>
    </div>
</body>
</html>