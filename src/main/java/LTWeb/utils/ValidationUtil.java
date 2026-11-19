package LTWeb.utils;

import java.util.regex.Pattern;

public class ValidationUtil {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^(0|\\+84)[0-9]{9}$");
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{4,20}$");
    private static final Pattern VIDEO_ID_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]{2,20}$");

    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        return !isEmpty(email) && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidPhone(String phone) {
        return !isEmpty(phone) && PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    public static boolean isValidUsername(String username) {
        return !isEmpty(username) && USERNAME_PATTERN.matcher(username.trim()).matches();
    }

    public static boolean isValidPassword(String password) {
        // Ít nhất 6 ký tự
        return !isEmpty(password) && password.length() >= 6;
    }

    public static boolean isValidVideoId(String videoId) {
        return !isEmpty(videoId) && VIDEO_ID_PATTERN.matcher(videoId.trim()).matches();
    }
}