package LTWeb.controllers;

import java.io.IOException;
import java.util.List;

import LTWeb.models.User;
import LTWeb.models.Video;
import LTWeb.services.IVideoService;
import LTWeb.services.UserService;
import LTWeb.services.UserServiceImpl;
import LTWeb.services.VideoServiceImpl;
import LTWeb.utils.Constant;
import LTWeb.utils.EmailUtil;
import LTWeb.utils.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = {
    "", "/home", "/login", "/register", "/verify-otp",
    "/forgot-password", "/reset-password", "/resend-otp", "/waiting", "/logout"
})
public class AuthController extends HttpServlet {
    private UserService userService = new UserServiceImpl();
    private IVideoService videoService = new VideoServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String path = req.getServletPath();

        switch (path) {
            case "/login":
                req.getRequestDispatcher(Constant.Path.LOGIN).forward(req, resp);
                return;
            case "/register":
                req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
                return;
            case "/verify-otp":
                req.getRequestDispatcher(Constant.Path.VERIFY_OTP).forward(req, resp);
                return;
            case "/forgot-password":
                req.getRequestDispatcher(Constant.Path.FORGOT_PASSWORD).forward(req, resp);
                return;
            case "/waiting":
                HttpSession s = req.getSession(false);
                if (s != null && s.getAttribute("account") != null) {
                    resp.sendRedirect(req.getContextPath() + "/home");
                } else {
                    resp.sendRedirect(req.getContextPath() + "/login");
                }
                return;
            case "/logout":
                HttpSession sess = req.getSession(false);
                if (sess != null) {
                    sess.invalidate();
                }
                Cookie[] ck = req.getCookies();
                if (ck != null) {
                    for (Cookie c : ck) {
                        if (Constant.COOKIE_REMEMBER.equals(c.getName())) {
                            c.setMaxAge(0);
                            c.setPath(req.getContextPath().isEmpty() ? "/" : req.getContextPath());
                            resp.addCookie(c);
                        }
                    }
                }
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            default:
                List<Video> top10Videos = videoService.findTop10Newest();
                req.setAttribute("top10Videos", top10Videos);
                req.getRequestDispatcher(Constant.Path.HOME).forward(req, resp);
                return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String path = req.getServletPath();

        if ("/login".equals(path)) {
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            boolean isRememberMe = "on".equals(req.getParameter("remember"));

            if (ValidationUtil.isEmpty(username) || ValidationUtil.isEmpty(password)) {
                req.setAttribute("alert", "Vui lòng nhập đầy đủ tài khoản và mật khẩu!");
                req.getRequestDispatcher(Constant.Path.LOGIN).forward(req, resp);
                return;
            }

            User user = userService.login(username, password);
            if (user == null) {
                req.setAttribute("alert", "Tài khoản hoặc mật khẩu không đúng!");
                req.getRequestDispatcher(Constant.Path.LOGIN).forward(req, resp);
                return;
            }

            if (user.getStatus() == 0) {
                String otpCode = EmailUtil.getRandomOTP();
                user.setCode(EmailUtil.packOTPWithExpiry(otpCode));
                userService.update(user);
                EmailUtil.sendOTP(user.getEmail(), "Mã OTP kích hoạt tài khoản LTWeb", otpCode);

                req.setAttribute("email", user.getEmail());
                req.setAttribute("alert", "Tài khoản chưa kích hoạt! Mã OTP mới (hiệu lực 60 giây) vừa được gửi.");
                req.getRequestDispatcher(Constant.Path.VERIFY_OTP).forward(req, resp);
                return;
            }

            HttpSession session = req.getSession(true);
            session.setAttribute("account", user);
            if (isRememberMe) {
                Cookie cookie = new Cookie(Constant.COOKIE_REMEMBER, username);
                cookie.setMaxAge(30 * 60);
                cookie.setPath(req.getContextPath().isEmpty() ? "/" : req.getContextPath());
                resp.addCookie(cookie);
            }
            resp.sendRedirect(req.getContextPath() + "/waiting");
            return;
        }

        if ("/register".equals(path)) {
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            String email = req.getParameter("email");
            String fullname = req.getParameter("fullname");
            String phone = req.getParameter("phone");

            if (!ValidationUtil.isValidUsername(username)) {
                req.setAttribute("alert", "Username phải từ 4-20 ký tự, không dấu, không khoảng trắng!");
                req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
                return;
            }
            if (!ValidationUtil.isValidPassword(password)) {
                req.setAttribute("alert", "Mật khẩu phải có ít nhất 6 ký tự!");
                req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
                return;
            }
            if (!ValidationUtil.isValidEmail(email)) {
                req.setAttribute("alert", "Định dạng Email không hợp lệ!");
                req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
                return;
            }
            if (ValidationUtil.isEmpty(fullname)) {
                req.setAttribute("alert", "Họ tên không được để trống!");
                req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
                return;
            }
            if (!ValidationUtil.isEmpty(phone) && !ValidationUtil.isValidPhone(phone)) {
                req.setAttribute("alert", "Số điện thoại phải gồm 10 số bắt đầu bằng 0!");
                req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
                return;
            }

            if (userService.checkExistEmail(email)) {
                req.setAttribute("alert", "Email đã tồn tại!");
                req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
                return;
            }
            if (userService.checkExistUsername(username)) {
                req.setAttribute("alert", "Tài khoản đã tồn tại!");
                req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
                return;
            }

            String otpCode = EmailUtil.getRandomOTP();
            if (userService.register(username, password, email, fullname, phone, otpCode)) {
                EmailUtil.sendOTP(email, "Mã OTP kích hoạt tài khoản LTWeb", otpCode);
                req.setAttribute("email", email);
                req.setAttribute("message", "Mã OTP đã gửi tới " + email + " (Hiệu lực trong 60 giây).");
                req.getRequestDispatcher(Constant.Path.VERIFY_OTP).forward(req, resp);
                return;
            }

            req.setAttribute("alert", "Lỗi hệ thống!");
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
            return;
        }

        if ("/verify-otp".equals(path)) {
            String email = req.getParameter("email");
            String otp = req.getParameter("otp");

            if (ValidationUtil.isEmpty(email) || ValidationUtil.isEmpty(otp)) {
                req.setAttribute("email", email);
                req.setAttribute("alert", "Vui lòng nhập đầy đủ Email và mã OTP!");
                req.getRequestDispatcher(Constant.Path.VERIFY_OTP).forward(req, resp);
                return;
            }

            String result = userService.verifyOTP(email, otp);
            if ("OK".equals(result)) {
                req.setAttribute("alert", "Kích hoạt tài khoản thành công! Mời bạn đăng nhập.");
                req.getRequestDispatcher(Constant.Path.LOGIN).forward(req, resp);
                return;
            }
            if ("EXPIRED".equals(result)) {
                req.setAttribute("email", email);
                req.setAttribute("alert", "Mã OTP đã hết hạn (quá 1 phút)! Vui lòng bấm 'Gửi lại mã OTP'.");
                req.getRequestDispatcher(Constant.Path.VERIFY_OTP).forward(req, resp);
                return;
            }

            req.setAttribute("email", email);
            req.setAttribute("alert", "Mã OTP không chính xác!");
            req.getRequestDispatcher(Constant.Path.VERIFY_OTP).forward(req, resp);
            return;
        }

        if ("/forgot-password".equals(path)) {
            String email = req.getParameter("email");

            if (!ValidationUtil.isValidEmail(email)) {
                req.setAttribute("alert", "Vui lòng nhập đúng định dạng Email!");
                req.getRequestDispatcher(Constant.Path.FORGOT_PASSWORD).forward(req, resp);
                return;
            }

            User user = userService.findByEmail(email);
            if (user == null) {
                req.setAttribute("alert", "Email không tồn tại trong hệ thống!");
                req.getRequestDispatcher(Constant.Path.FORGOT_PASSWORD).forward(req, resp);
                return;
            }

            String otpCode = EmailUtil.getRandomOTP();
            user.setCode(EmailUtil.packOTPWithExpiry(otpCode));
            userService.update(user);
            EmailUtil.sendOTP(email, "Mã OTP đặt lại mật khẩu LTWeb (Hiệu lực 1 phút)", otpCode);

            req.setAttribute("email", email);
            req.setAttribute("step", "reset");
            req.setAttribute("message", "Mã OTP đã được gửi tới " + email + ". Vui lòng nhập trong vòng 60 giây!");
            req.getRequestDispatcher(Constant.Path.FORGOT_PASSWORD).forward(req, resp);
            return;
        }

        if ("/reset-password".equals(path)) {
            String email = req.getParameter("email");
            String otp = req.getParameter("otp");
            String newPassword = req.getParameter("newPassword");
            String confirmPassword = req.getParameter("confirmPassword");

            if (!ValidationUtil.isValidPassword(newPassword)) {
                req.setAttribute("email", email);
                req.setAttribute("step", "reset");
                req.setAttribute("alert", "Mật khẩu mới phải có ít nhất 6 ký tự!");
                req.getRequestDispatcher(Constant.Path.FORGOT_PASSWORD).forward(req, resp);
                return;
            }

            if (newPassword == null || !newPassword.equals(confirmPassword)) {
                req.setAttribute("email", email);
                req.setAttribute("step", "reset");
                req.setAttribute("alert", "Mật khẩu xác nhận không khớp!");
                req.getRequestDispatcher(Constant.Path.FORGOT_PASSWORD).forward(req, resp);
                return;
            }

            String result = userService.resetPassword(email, otp, newPassword);
            if ("OK".equals(result)) {
                req.setAttribute("alert", "Đặt lại mật khẩu thành công! Mời bạn đăng nhập.");
                req.getRequestDispatcher(Constant.Path.LOGIN).forward(req, resp);
                return;
            }
            if ("EXPIRED".equals(result)) {
                req.setAttribute("email", email);
                req.setAttribute("step", "reset");
                req.setAttribute("alert", "Mã OTP đã hết hạn (quá 60 giây)! Hãy bấm 'Gửi lại mã OTP mới'.");
                req.getRequestDispatcher(Constant.Path.FORGOT_PASSWORD).forward(req, resp);
                return;
            }

            req.setAttribute("email", email);
            req.setAttribute("step", "reset");
            req.setAttribute("alert", "Mã OTP không chính xác!");
            req.getRequestDispatcher(Constant.Path.FORGOT_PASSWORD).forward(req, resp);
            return;
        }

        if ("/resend-otp".equals(path)) {
            String email = req.getParameter("email");
            String type = req.getParameter("type"); // "verify" hoặc "forgot"
            User user = userService.findByEmail(email);

            if (user != null) {
                String otpCode = EmailUtil.getRandomOTP();
                user.setCode(EmailUtil.packOTPWithExpiry(otpCode));
                userService.update(user);
                EmailUtil.sendOTP(email, "Mã OTP mới từ LTWeb (Hiệu lực 1 phút)", otpCode);
            }

            req.setAttribute("email", email);
            req.setAttribute("message", "Đã gửi lại mã OTP mới! Bạn có 60 giây để nhập.");
            if ("verify".equals(type)) {
                req.getRequestDispatcher(Constant.Path.VERIFY_OTP).forward(req, resp);
            } else {
                req.setAttribute("step", "reset");
                req.getRequestDispatcher(Constant.Path.FORGOT_PASSWORD).forward(req, resp);
            }
        }
    }
}