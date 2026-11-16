<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quên mật khẩu - Xác thực OTP</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f5f6fa; display: flex; justify-content: center; padding-top: 50px; }
        .card { background: #fff; padding: 25px 30px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); width: 400px; }
        input[type="email"], input[type="text"], input[type="password"] {
            width: 100%; padding: 10px; margin: 6px 0 14px 0; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box;
        }
        button { width: 100%; padding: 10px; background: #007bff; color: #fff; border: none; border-radius: 4px; cursor: pointer; font-weight: bold; }
        button:disabled { background: #aaa; cursor: not-allowed; }
        .btn-resend { background: #28a745; margin-top: 10px; }
        .timer { font-weight: bold; color: #d9534f; margin-bottom: 12px; text-align: center; }
        .msg-success { color: #155724; background: #d4edda; padding: 8px; border-radius: 4px; margin-bottom: 10px; }
        .msg-error { color: #721c24; background: #f8d7da; padding: 8px; border-radius: 4px; margin-bottom: 10px; }
    </style>
</head>
<body>
    <div class="card">
        <h2 style="margin-top:0;">Khôi phục mật khẩu</h2>

        <c:if test="${not empty message}"><div class="msg-success">${message}</div></c:if>
        <c:if test="${not empty alert}"><div class="msg-error">${alert}</div></c:if>

        <c:choose>
            <c:when test="${step == 'reset'}">
                <div class="timer">
                    Thời gian hiệu lực OTP còn: <span id="countdown">60</span> giây
                </div>

                <form action="${pageContext.request.contextPath}/reset-password" method="post">
                    <label>Email nhận OTP:</label>
                    <input type="email" name="email" value="${email}" readonly />

                    <label>Mã OTP (6 chữ số):</label>
                    <input type="text" id="otpInput" name="otp" maxlength="6" placeholder="Nhập 6 số OTP..." required />

                    <label>Mật khẩu mới:</label>
                    <input type="password" name="newPassword" placeholder="Nhập mật khẩu mới..." required />

                    <label>Nhập lại mật khẩu mới:</label>
                    <input type="password" name="confirmPassword" placeholder="Xác nhận mật khẩu mới..." required />

                    <button type="submit" id="submitBtn">Xác nhận đổi mật khẩu</button>
                </form>

                <!-- Form Gửi lại mã OTP khi hết 60 giây -->
                <form action="${pageContext.request.contextPath}/resend-otp" method="post">
                    <input type="hidden" name="email" value="${email}" />
                    <input type="hidden" name="type" value="forgot" />
                    <button type="submit" id="resendBtn" class="btn-resend" disabled>Gửi lại mã OTP mới (60s)</button>
                </form>

                <script>
                    let timeLeft = 60;
                    const countdownEl = document.getElementById('countdown');
                    const submitBtn = document.getElementById('submitBtn');
                    const resendBtn = document.getElementById('resendBtn');

                    const timer = setInterval(() => {
                        timeLeft--;
                        countdownEl.textContent = timeLeft;
                        resendBtn.textContent = "Gửi lại mã OTP mới (" + timeLeft + "s)";

                        if (timeLeft <= 0) {
                            clearInterval(timer);
                            countdownEl.textContent = "Đã hết hạn!";
                            submitBtn.disabled = true;
                            submitBtn.textContent = "Mã OTP đã hết hạn (Quá 1 phút)";
                            resendBtn.disabled = false;
                            resendBtn.textContent = "Gửi lại mã OTP mới ngay";
                        }
                    }, 1000);
                </script>
            </c:when>

            <c:otherwise>
                <form action="${pageContext.request.contextPath}/forgot-password" method="post">
                    <label>Nhập Email đã đăng ký tài khoản:</label>
                    <input type="email" name="email" placeholder="example@gmail.com" required />
                    <button type="submit">Gửi mã xác nhận OTP</button>
                </form>
            </c:otherwise>
        </c:choose>

        <div style="margin-top: 15px; text-align: center;">
            <a href="${pageContext.request.contextPath}/login">← Quay lại Đăng nhập</a>
        </div>
    </div>
</body>
</html>