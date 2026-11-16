package LTWeb.services;

import LTWeb.models.User;

public interface UserService {
    User login(String username, String password);
    User get(String username);
    User findByEmail(String email);
    void insert(User user);
    void update(User user);
    boolean register(String username, String password, String email, String fullname, String phone, String otpCode);
    String verifyOTP(String email, String otpCode);
    String resetPassword(String email, String otpCode, String newPassword);
    boolean checkExistEmail(String email);
    boolean checkExistUsername(String username);
    boolean checkExistPhone(String phone);
}