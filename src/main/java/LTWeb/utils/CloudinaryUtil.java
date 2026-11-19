package LTWeb.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.servlet.http.Part;

public class CloudinaryUtil {
    private static final Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
        "cloud_name", "hucax6bc",
        "api_key", "852199566416172",
        "api_secret", "852199566416172",
        "secure", true
    ));

    public static String uploadImage(Part filePart) throws IOException {
        if (filePart == null || filePart.getSize() == 0) {
            return null;
        }
        try (InputStream is = filePart.getInputStream()) {
            byte[] bytes = is.readAllBytes();
            Map<?, ?> uploadResult = cloudinary.uploader().upload(bytes, ObjectUtils.asMap(
                "folder", "ltweb_avatars",
                "resource_type", "image"
            ));
            return (String) uploadResult.get("secure_url");
        }
    }
}