package LTWeb.utils;

import java.util.Properties;
import java.util.Random;
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class EmailUtil {
	private static final String FROM_EMAIL = "ngocdeptraibodoivodich@gmail.com";
    private static final String APP_PASSWORD = "rvfv hvdf awgh lkqy";
    public static final long OTP_VALID_DURATION = 60 * 1000; // Đúng 1 phút 

    public static String getRandomOTP() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }

    // Ghép mã OTP và thời điểm hết hạn (hiện tại + 60 giây) để lưu vào DB
    public static String packOTPWithExpiry(String otp) {
        long expiryTime = System.currentTimeMillis() + OTP_VALID_DURATION;
        return otp + ":" + expiryTime;
    }

    // Kiểm tra OTP: Trả về "OK", "EXPIRED" (quá 1 phút), hoặc "INVALID" (sai mã)
    public static String validateOTP(String storedCode, String inputOtp) {
        if (storedCode == null || inputOtp == null || inputOtp.trim().isEmpty()) {
            return "INVALID";
        }
        String[] parts = storedCode.split(":");
        String actualOtp = parts[0];

        if (!actualOtp.equals(inputOtp.trim())) {
            return "INVALID";
        }
        if (parts.length > 1) {
            long expiryTime = Long.parseLong(parts[1]);
            if (System.currentTimeMillis() > expiryTime) {
                return "EXPIRED";
            }
        }
        return "OK";
    }

    public static boolean sendOTP(String toEmail, String subject, String otpCode) {
        System.out.println("==========================================");
        System.out.println("[EMAIL OTP - HẠN 60 GIÂY] Tới: " + toEmail + " | Mã OTP: " + otpCode);
        System.out.println("==========================================");

        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL, "LTWeb System", "UTF-8"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(MimeUtility.encodeText(subject, "UTF-8", "B"));
            message.setContent(
                "<div style='font-family:Arial,sans-serif;padding:15px;border:1px solid #ddd;'>"
                + "<h3>Mã xác nhận OTP của bạn là: <b style='color:#d9534f;font-size:22px;'>" + otpCode + "</b></h3>"
                + "<p>Mã này chỉ có hiệu lực trong vòng <b>1 phút (60 giây)</b>. Tuyệt đối không chia sẻ mã này cho người khác.</p>"
                + "</div>",
                "text/html; charset=UTF-8"
            );

            Transport.send(message);
            return true;
        } catch (Exception e) {
            System.out.println("Lấy mã OTP trực tiếp trên Console để test: " + otpCode);
            return true;
        }
    }
}