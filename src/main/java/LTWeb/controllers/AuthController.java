package LTWeb.controllers;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import LTWeb.models.User;
import LTWeb.services.UserService;
import LTWeb.services.UserServiceImpl;
import LTWeb.utils.Constant;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = {"", "/home", "/login", "/register", "/waiting", "/logout"})
public class AuthController extends HttpServlet {
    private UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        switch (path) {
            case "/login":
                HttpSession session = req.getSession(false);
                if (session != null && session.getAttribute("account") != null) {
                    resp.sendRedirect(req.getContextPath() + "/waiting");
                    return;
                }
                Cookie[] cookies = req.getCookies();
                if (cookies != null) {
                    for (Cookie c : cookies) {
                        if (Constant.COOKIE_REMEMBER.equals(c.getName())) {
                            User u = userService.get(c.getValue());
                            if (u != null) {
                                session = req.getSession(true);
                                session.setAttribute("account", u);
                                resp.sendRedirect(req.getContextPath() + "/waiting");
                                return;
                            }
                        }
                    }
                }
                req.getRequestDispatcher(Constant.Path.LOGIN).forward(req, resp);
                break;

            case "/register":
                req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
                break;

            case "/waiting":
                HttpSession s = req.getSession(false);
                if (s != null && s.getAttribute("account") != null) {
                    resp.sendRedirect(req.getContextPath() + "/admin/categories");
                } else {
                    resp.sendRedirect(req.getContextPath() + "/login");
                }
                break;

            case "/logout":
                HttpSession sess = req.getSession(false);
                if (sess != null) sess.invalidate();
                Cookie[] ck = req.getCookies();
                if (ck != null) {
                    for (Cookie c : ck) {
                        if (Constant.COOKIE_REMEMBER.equals(c.getName())) {
                            c.setMaxAge(0);
                            resp.addCookie(c);
                        }
                    }
                }
                resp.sendRedirect(req.getContextPath() + "/login");
                break;

            default: // "/" và "/home"
                req.getRequestDispatcher(Constant.Path.HOME).forward(req, resp);
                break;
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

            User user = userService.login(username, password);
            if (user != null) {
                HttpSession session = req.getSession(true);
                session.setAttribute("account", user);
                if (isRememberMe) {
                    Cookie cookie = new Cookie(Constant.COOKIE_REMEMBER, username);
                    cookie.setMaxAge(30 * 60);
                    resp.addCookie(cookie);
                }
                resp.sendRedirect(req.getContextPath() + "/waiting");
            } else {
                req.setAttribute("alert", "Tài khoản hoặc mật khẩu không đúng!");
                req.getRequestDispatcher(Constant.Path.LOGIN).forward(req, resp);
            }
        } else if ("/register".equals(path)) {
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            String email = req.getParameter("email");
            String fullname = req.getParameter("fullname");
            String phone = req.getParameter("phone");

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
            if (userService.register(username, password, email, fullname, phone)) {
                resp.sendRedirect(req.getContextPath() + "/login");
            } else {
                req.setAttribute("alert", "Lỗi hệ thống!");
                req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
            }
        }
    }
}