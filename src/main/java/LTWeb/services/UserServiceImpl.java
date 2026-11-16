package LTWeb.services;

import LTWeb.models.User;
import LTWeb.repository.UserDao;
import LTWeb.repository.UserDaoImpl;
import LTWeb.utils.EmailUtil;
import LTWeb.utils.PasswordUtil;

public class UserServiceImpl implements UserService {
    UserDao userDao = new UserDaoImpl();

    @Override
    public User login(String username, String password) {
        User user = this.get(username);
        if (user != null) {
            String hashedInput = PasswordUtil.hashPassword(password);
            if (hashedInput.equals(user.getPassWord()) || password.equals(user.getPassWord())) {
                return user;
            }
        }
        return null;
    }

    @Override
    public User get(String username) { return userDao.get(username); }

    @Override
    public User findByEmail(String email) { return userDao.findByEmail(email); }

    @Override
    public boolean register(String username, String password, String email, String fullname, String phone, String otpCode) {
        if (userDao.checkExistUsername(username) || userDao.checkExistEmail(email)) {
            return false;
        }
        long millis = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(millis);
        int role = "admin".equalsIgnoreCase(username) ? 1 : 5;
        String hashedPassword = PasswordUtil.hashPassword(password);

        // Đóng gói OTP kèm hạn 1 phút
        String packedOtp = EmailUtil.packOTPWithExpiry(otpCode);
        User newUser = new User(email, username, fullname, hashedPassword, null, role, phone, date, 0, packedOtp);
        userDao.insert(newUser);
        return true;
    }

    @Override
    public String verifyOTP(String email, String otpCode) {
        User user = userDao.findByEmail(email);
        if (user == null) return "INVALID";

        String status = EmailUtil.validateOTP(user.getCode(), otpCode);
        if ("OK".equals(status)) {
            user.setStatus(1);
            user.setCode(null); // Xóa ngay OTP sau khi kích hoạt thành công
            userDao.update(user);
        }
        return status;
    }

    @Override
    public String resetPassword(String email, String otpCode, String newPassword) {
        User user = userDao.findByEmail(email);
        if (user == null) return "INVALID";

        String status = EmailUtil.validateOTP(user.getCode(), otpCode);
        if ("OK".equals(status)) {
            user.setPassWord(PasswordUtil.hashPassword(newPassword));
            user.setCode(null); // Vô hiệu hóa mã OTP ngay sau khi đổi mật khẩu
            user.setStatus(1);
            userDao.update(user);
        }
        return status;
    }

    @Override
    public boolean checkExistEmail(String email) { return userDao.checkExistEmail(email); }

    @Override
    public boolean checkExistUsername(String username) { return userDao.checkExistUsername(username); }

    @Override
    public boolean checkExistPhone(String phone) { return userDao.checkExistPhone(phone); }

    @Override
    public void insert(User user) { userDao.insert(user); }

    @Override
    public void update(User user) { userDao.update(user); }
}