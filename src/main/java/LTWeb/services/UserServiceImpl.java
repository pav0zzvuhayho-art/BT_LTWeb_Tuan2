package LTWeb.services;

import LTWeb.models.User;
import LTWeb.repository.UserDao;
import LTWeb.repository.UserDaoImpl;

public class UserServiceImpl implements UserService {
    UserDao userDao = new UserDaoImpl();

    @Override
    public User login(String username, String password) {
        User user = this.get(username);
        if (user != null && password.equals(user.getPassWord())) {
            return user;
        }
        return null;
    }

    @Override
    public User get(String username) { return userDao.get(username); }

    @Override
    public boolean register(String username, String password, String email, String fullname, String phone) {
        if (userDao.checkExistUsername(username)) return false;
        long millis = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(millis);
        int role = "admin".equalsIgnoreCase(username) ? 1 : 5;
        userDao.insert(new User(email, username, fullname, password, null, role, phone, date));
        return true;
    }

    @Override
    public boolean checkExistEmail(String email) { return userDao.checkExistEmail(email); }

    @Override
    public boolean checkExistUsername(String username) { return userDao.checkExistUsername(username); }

    @Override
    public boolean checkExistPhone(String phone) { return userDao.checkExistPhone(phone); }

    @Override
    public void insert(User user) { userDao.insert(user); }
}