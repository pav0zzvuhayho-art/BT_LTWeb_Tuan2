<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark shadow-sm">
    <div class="container">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/home">
            <i class="bi bi-play-btn-fill text-danger"></i> LTWeb Video
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#webNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="webNav">
            <ul class="navbar-nav me-auto">
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/home">Trang chủ</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/video">Tất cả Video</a></li>
                <c:if test="${not empty sessionScope.account}">
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/admin/videos">Quản lý Video</a></li>
                </c:if>
            </ul>
            <ul class="navbar-nav ms-auto align-items-center">
                <c:choose>
                    <c:when test="${not empty sessionScope.account}">
                        <li class="nav-item me-2">
                            <a class="nav-link text-white" href="${pageContext.request.contextPath}/profile">
                                <i class="bi bi-person-circle"></i> ${sessionScope.account.fullName}
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="btn btn-outline-danger btn-sm" href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item me-2"><a class="btn btn-outline-light btn-sm" href="${pageContext.request.contextPath}/login">Đăng nhập</a></li>
                        <li class="nav-item"><a class="btn btn-primary btn-sm" href="${pageContext.request.contextPath}/register">Đăng ký</a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>