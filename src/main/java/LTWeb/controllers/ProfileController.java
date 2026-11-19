package LTWeb.controllers;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import LTWeb.models.User;
import LTWeb.services.UserService;
import LTWeb.services.UserServiceImpl;
import LTWeb.utils.CloudinaryUtil;
import LTWeb.utils.EmailUtil;
import LTWeb.utils.ValidationUtil;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/profile", "/profile/update", "/profile/verify-email" })
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,
    maxFileSize = 1024 * 1024 * 10,
    maxRequestSize = 1024 * 1024 * 20
)
public class ProfileController extends HttpServlet {
    private UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("account") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        req.getRequestDispatcher("/views/profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("account") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("account");
        String path = req.getServletPath();

        if ("/profile/update".equals(path)) {
            String fullname = req.getParameter("fullname");
            String phone = req.getParameter("phone");
            String newEmail = req.getParameter("email");

            // Validation
            if (ValidationUtil.isEmpty(fullname)) {
                req.setAttribute("alert", "Họ tên không được để trống!");
                req.getRequestDispatcher("/views/profile.jsp").forward(req, resp);
                return;
            }
            if (!ValidationUtil.isEmpty(phone) && !ValidationUtil.isValidPhone(phone)) {
                req.setAttribute("alert", "Số điện thoại không hợp lệ (10 chữ số bắt đầu bằng 0)!");
                req.getRequestDispatcher("/views/profile.jsp").forward(req, resp);
                return;
            }
            if (!ValidationUtil.isValidEmail(newEmail)) {
                req.setAttribute("alert", "Định dạng Email không hợp lệ!");
                req.getRequestDispatcher("/views/profile.jsp").forward(req, resp);
                return;
            }

            currentUser.setFullName(fullname.trim());
            currentUser.setPhone(phone != null ? phone.trim() : null);

            Part avatarPart = req.getPart("avatarFile");
            if (avatarPart != null && avatarPart.getSize() > 0) {
                try {
                    String cloudUrl = CloudinaryUtil.uploadImage(avatarPart);
                    if (cloudUrl != null) {
                        currentUser.setAvatar(cloudUrl);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    req.setAttribute("alert", "Lỗi upload ảnh lên Cloudinary: " + e.getMessage());
                    req.getRequestDispatcher("/views/profile.jsp").forward(req, resp);
                    return;
                }
            }

            if (!newEmail.trim().equalsIgnoreCase(currentUser.getEmail())) {
                if (userService.checkExistEmail(newEmail.trim())) {
                    req.setAttribute("alert", "Email mới đã được sử dụng bởi tài khoản khác!");
                    req.getRequestDispatcher("/views/profile.jsp").forward(req, resp);
                    return;
                }
                userService.update(currentUser);
                session.setAttribute("account", currentUser);

                String otpCode = EmailUtil.getRandomOTP();
                session.setAttribute("pendingNewEmail", newEmail.trim());
                session.setAttribute("pendingEmailOtp", EmailUtil.packOTPWithExpiry(otpCode));

                EmailUtil.sendOTP(newEmail.trim(), "Mã OTP xác nhận đổi Email mới - LTWeb", otpCode);

                req.setAttribute("needVerifyEmail", true);
                req.setAttribute("message", "Đã lưu thông tin & ảnh. Mã OTP đã gửi tới email mới (" + newEmail + "), vui lòng xác nhận trong 60 giây!");
                req.getRequestDispatcher("/views/profile.jsp").forward(req, resp);
                return;
            }

            userService.update(currentUser);
            session.setAttribute("account", currentUser);
            req.setAttribute("message", "Cập nhật Profile thành công!");
            req.getRequestDispatcher("/views/profile.jsp").forward(req, resp);
        }

        else if ("/profile/verify-email".equals(path)) {
            String inputOtp = req.getParameter("otp");
            String pendingEmail = (String) session.getAttribute("pendingNewEmail");
            String storedOtp = (String) session.getAttribute("pendingEmailOtp");

            String status = EmailUtil.validateOTP(storedOtp, inputOtp);
            if ("OK".equals(status) && pendingEmail != null) {
                currentUser.setEmail(pendingEmail);
                userService.update(currentUser);
                session.setAttribute("account", currentUser);

                session.removeAttribute("pendingNewEmail");
                session.removeAttribute("pendingEmailOtp");
                req.setAttribute("message", "Xác nhận OTP thành công! Email mới đã được cập nhật.");
            } else if ("EXPIRED".equals(status)) {
                req.setAttribute("needVerifyEmail", true);
                req.setAttribute("alert", "Mã OTP đã hết hạn (quá 60 giây)! Vui lòng bấm Cập nhật lại.");
            } else {
                req.setAttribute("needVerifyEmail", true);
                req.setAttribute("alert", "Mã OTP không chính xác!");
            }
            req.getRequestDispatcher("/views/profile.jsp").forward(req, resp);
        }
    }
}