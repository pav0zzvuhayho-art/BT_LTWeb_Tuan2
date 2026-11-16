<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Danh sách Video </title>
    <style>
        .grid { display: flex; flex-wrap: wrap; gap: 20px; }
        .card { border: 1px solid #ccc; padding: 10px; width: 220px; text-align: center; }
        .pagination a { padding: 6px 12px; border: 1px solid #333; margin: 2px; text-decoration: none; }
        .pagination a.active { background: #333; color: #fff; }
    </style>
</head>
<body>
    <a href="${pageContext.request.contextPath}/home">← Về trang chủ</a> |
    <a href="${pageContext.request.contextPath}/video">Tất cả Video</a>
    <c:if test="${not empty sessionScope.account}">
        | <a href="${pageContext.request.contextPath}/video?uid=${sessionScope.account.id}">Video của tôi</a>
    </c:if>
    <hr/>

    <h2>Danh sách Video</h2>
    <div class="grid">
        <c:forEach items="${videoList}" var="v">
            <div class="card">
                <c:url value="/image?fname=${v.poster}" var="posterUrl" />
                <img src="${v.poster != null && v.poster.startsWith('http') ? v.poster : posterUrl}" width="200" height="120" />
                <h4>
                    <a href="${pageContext.request.contextPath}/video/detail?id=${v.videoId}">${v.title}</a>
                </h4>
                <p>Người đăng: ${v.user != null ? v.user.userName : 'Admin'} | Views: ${v.views}</p>
            </div>
        </c:forEach>
    </div>

    <div class="pagination" style="margin-top: 20px;">
        <c:forEach begin="1" end="${totalPages}" var="i">
            <a class="${i == currentPage ? 'active' : ''}" 
               href="${pageContext.request.contextPath}/video?page=${i}&uid=${param.uid}">${i}</a>
        </c:forEach>
    </div>
</body>
</html>