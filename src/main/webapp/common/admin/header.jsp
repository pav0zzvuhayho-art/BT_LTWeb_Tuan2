<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/common/taglib.jsp" %>

<nav class="navbar navbar-expand-lg navbar-dark bg-primary shadow-sm">
    <div class="container">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/admin/videos">
            <i class="bi bi-speedometer2"></i> ADMIN PANEL
        </a>
        <div class="collapse navbar-collapse">
            <ul class="navbar-nav me-auto">
                <li class="nav-item"><a class="nav-link text-white" href="${pageContext.request.contextPath}/home">Xem Trang Chủ</a></li>
                <li class="nav-item"><a class="nav-link text-white" href="${pageContext.request.contextPath}/admin/videos">Quản lý Videos</a></li>
            </ul>
            <a class="btn btn-light btn-sm" href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
        </div>
    </div>
</nav>