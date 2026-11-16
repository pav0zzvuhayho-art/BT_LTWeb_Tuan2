package LTWeb.repository;

import LTWeb.models.User;

public interface UserDao {
    User get(String username);
    User findByEmail(String email);
    void insert(User user);
    void update(User user);
    boolean checkExistEmail(String email);
    boolean checkExistUsername(String username);
    boolean checkExistPhone(String phone);
}