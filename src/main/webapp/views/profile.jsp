<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Thông tin cá nhân (Profile)</title>
</head>
<body>
    <div class="row justify-content-center">
        <div class="col-md-7">
            <div class="card shadow-sm">
                <div class="card-header bg-primary text-white">
                    <h4 class="mb-0"><i class="bi bi-person-lines-fill"></i> Cập nhật Profile</h4>
                </div>
                <div class="card-body">
                    <c:if test="${not empty message}">
                        <div class="alert alert-success">${message}</div>
                    </c:if>
                    <c:if test="${not empty alert}">
                        <div class="alert alert-danger">${alert}</div>
                    </c:if>

                    <div class="text-center mb-3">
                        <c:choose>
                            <c:when test="${not empty sessionScope.account.avatar}">
                                <img src="${sessionScope.account.avatar}" class="rounded-circle border" width="120" height="120" style="object-fit: cover;">
                            </c:when>
                            <c:otherwise>
                                <img src="https://via.placeholder.com/120?text=Avatar" class="rounded-circle border" width="120" height="120">
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <c:if test="${needVerifyEmail == true}">
                        <div class="alert alert-warning">
                            <strong>Xác nhận Email mới:</strong> ${sessionScope.pendingNewEmail}
                            <form action="${pageContext.request.contextPath}/profile/verify-email" method="post" class="mt-2 d-flex gap-2">
                                <input type="text" name="otp" class="form-control" maxlength="6" placeholder="Nhập 6 số OTP từ email mới..." required>
                                <button type="submit" class="btn btn-success text-nowrap">Xác nhận OTP</button>
                            </form>
                        </div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/profile/update" method="post" enctype="multipart/form-data" class="needs-validation" novalidate>
                        <div class="mb-3">
                            <label class="form-label">Tên đăng nhập (Username)</label>
                            <input type="text" class="form-control" value="${sessionScope.account.userName}" readonly disabled>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Họ và tên (Fullname)</label>
                            <input type="text" name="fullname" class="form-control" value="${sessionScope.account.fullName}" required>
                            <div class="invalid-feedback">Vui lòng nhập họ và tên.</div>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Số điện thoại (Phone)</label>
                            <input type="text" name="phone" class="form-control" value="${sessionScope.account.phone}" pattern="^(0|\+84)[0-9]{9}$">
                            <div class="invalid-feedback">Số điện thoại phải gồm 10 số bắt đầu bằng 0.</div>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Email (Nếu đổi sang email mới, hệ thống sẽ gửi OTP xác nhận)</label>
                            <input type="email" name="email" class="form-control" value="${sessionScope.account.email}" required>
                            <div class="invalid-feedback">Vui lòng nhập đúng định dạng Email.</div>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Ảnh đại diện mới (Upload lên Cloudinary)</label>
                            <input type="file" name="avatarFile" class="form-control" accept="image/*">
                        </div>

                        <button type="submit" class="btn btn-primary w-100">Lưu thay đổi</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</body>
</html>