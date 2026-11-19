<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><sitemesh:write property="title" default="LTWeb Video System"/></title>
    <!-- Bootstrap 5.3 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <sitemesh:write property="head"/>
</head>
<body class="d-flex flex-column min-vh-100 bg-light">

    <!-- Navbar Bootstrap -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark shadow-sm">
        <div class="container">
            <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/home">
                <i class="bi bi-play-btn-fill text-danger"></i> LTWeb Video
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#mainNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="mainNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/home">Trang chủ</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/video">Tất cả Video</a>
                    </li>
                    <c:if test="${not empty sessionScope.account}">
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/admin/videos">Quản lý Video</a>
                        </li>
                    </c:if>
                </ul>
                <ul class="navbar-nav ms-auto align-items-center">
                    <c:choose>
                        <c:when test="${not empty sessionScope.account}">
                            <li class="nav-item me-2">
                                <a class="nav-link text-white" href="${pageContext.request.contextPath}/profile">
                                    <c:choose>
                                        <c:when test="${not empty sessionScope.account.avatar}">
                                            <img src="${sessionScope.account.avatar}" class="rounded-circle me-1" width="28" height="28" style="object-fit:cover;">
                                        </c:when>
                                        <c:otherwise><i class="bi bi-person-circle"></i></c:otherwise>
                                    </c:choose>
                                    ${sessionScope.account.fullName}
                                </a>
                            </li>
                            <li class="nav-item">
                                <a class="btn btn-outline-danger btn-sm" href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="nav-item me-2">
                                <a class="btn btn-outline-light btn-sm" href="${pageContext.request.contextPath}/login">Đăng nhập</a>
                            </li>
                            <li class="nav-item">
                                <a class="btn btn-primary btn-sm" href="${pageContext.request.contextPath}/register">Đăng ký</a>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </div>
    </nav>

    <main class="container my-4 flex-grow-1">
        <sitemesh:write property="body"/>
    </main>

    <footer class="bg-dark text-white text-center py-3 mt-auto">
        <div class="container">
            <small>&copy; 2026 LTWeb - HCMUTE | SiteMesh 3 + Bootstrap 5</small>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
	(() => {
	  'use strict';
	  const forms = document.querySelectorAll('.needs-validation');
	  Array.from(forms).forEach(form => {
	    form.addEventListener('submit', event => {
	      if (!form.checkValidity()) {
	        event.preventDefault();
	        event.stopPropagation();
	      }
	      form.classList.add('was-validated');
	    }, false);
	  });
	})();
</script>
</body>
</html>