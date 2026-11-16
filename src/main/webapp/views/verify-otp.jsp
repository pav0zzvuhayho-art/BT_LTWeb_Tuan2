<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Kích hoạt tài khoản OTP</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f5f6fa; display: flex; justify-content: center; padding-top: 50px; }
        .card { background: #fff; padding: 25px 30px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); width: 400px; }
        input { width: 100%; padding: 10px; margin: 6px 0 14px 0; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; }
        button { width: 100%; padding: 10px; background: #007bff; color: #fff; border: none; border-radius: 4px; cursor: pointer; font-weight: bold; }
        button:disabled { background: #aaa; cursor: not-allowed; }
        .btn-resend { background: #28a745; margin-top: 10px; }
    </style>
</head>
<body>
    <div class="card">
        <h2>Kích hoạt tài khoản</h2>
        <c:if test="${not empty message}"><p style="color: green;">${message}</p></c:if>
        <c:if test="${not empty alert}"><p style="color: red;">${alert}</p></c:if>

        <p style="text-align:center; font-weight:bold; color:#d9534f;">
            Hiệu lực OTP còn: <span id="countdown">60</span> giây
        </p>

        <form action="${pageContext.request.contextPath}/verify-otp" method="post">
            <label>Email đăng ký:</label>
            <input type="email" name="email" value="${email}" readonly required />

            <label>Nhập mã OTP (6 số):</label>
            <input type="text" name="otp" maxlength="6" placeholder="Nhập mã OTP..." required />

            <button type="submit" id="verifyBtn">Kích hoạt tài khoản</button>
        </form>

        <form action="${pageContext.request.contextPath}/resend-otp" method="post">
            <input type="hidden" name="email" value="${email}" />
            <input type="hidden" name="type" value="verify" />
            <button type="submit" id="resendBtn" class="btn-resend" disabled>Gửi lại mã OTP (60s)</button>
        </form>

        <script>
            let t = 60;
            const cd = document.getElementById('countdown');
            const vBtn = document.getElementById('verifyBtn');
            const rBtn = document.getElementById('resendBtn');
            const interval = setInterval(() => {
                t--;
                cd.textContent = t;
                rBtn.textContent = "Gửi lại mã OTP (" + t + "s)";
                if (t <= 0) {
                    clearInterval(interval);
                    cd.textContent = "Hết hạn!";
                    vBtn.disabled = true;
                    rBtn.disabled = false;
                    rBtn.textContent = "Gửi lại mã OTP mới ngay";
                }
            }, 1000);
        </script>
    </div>
</body>
</html>