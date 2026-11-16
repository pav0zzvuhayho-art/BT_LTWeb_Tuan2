package LTWeb.controllers;

import java.io.File;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import LTWeb.models.User;
import LTWeb.models.Video;
import LTWeb.services.*;
import LTWeb.utils.Constant;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = {
    "/video", "/video/detail", "/admin/videos", 
    "/admin/video/add", "/admin/video/insert", 
    "/admin/video/edit", "/admin/video/update", "/admin/video/delete"
})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,
    maxFileSize = -1L,
    maxRequestSize = -1L
)
public class VideoController extends HttpServlet {
    private IVideoService videoService = new VideoServiceImpl();

    private String getFileName(Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf("=") + 2, content.length() - 1);
            }
        }
        return Constant.DEFAULT_FILENAME;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String path = req.getServletPath();

        if ("/video".equals(path)) {
            int page = 1;
            int pageSize = 6;
            if (req.getParameter("page") != null) page = Integer.parseInt(req.getParameter("page"));
            Integer userId = null;
            if (req.getParameter("uid") != null && !req.getParameter("uid").isEmpty()) {
                userId = Integer.parseInt(req.getParameter("uid"));
            }

            List<Video> list = videoService.findPaginated(page, pageSize, userId);
            int totalVideos = videoService.count(userId);
            int totalPages = (int) Math.ceil((double) totalVideos / pageSize);

            req.setAttribute("videoList", list);
            req.setAttribute("currentPage", page);
            req.setAttribute("totalPages", totalPages);
            req.getRequestDispatcher("/views/video-page.jsp").forward(req, resp);
        } else if ("/video/detail".equals(path)) {
            String id = req.getParameter("id");
            Video video = videoService.findById(id);
            if (video != null) {
                video.setViews(video.getViews() + 1);
                videoService.update(video);
            }
            req.setAttribute("video", video);
            req.getRequestDispatcher("/views/video-detail.jsp").forward(req, resp);
        } else if ("/admin/videos".equals(path)) {
            req.setAttribute("videoList", videoService.findAll());
            req.getRequestDispatcher("/views/admin/video-list.jsp").forward(req, resp);
        } else if ("/admin/video/edit".equals(path)) {
            String id = req.getParameter("id");
            req.setAttribute("video", videoService.findById(id));
            req.setAttribute("videoList", videoService.findAll());
            req.getRequestDispatcher("/views/admin/video-list.jsp").forward(req, resp);
        } else if ("/admin/video/delete".equals(path)) {
            String id = req.getParameter("id");
            videoService.delete(id);
            resp.sendRedirect(req.getContextPath() + "/admin/videos");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String path = req.getServletPath();

        File uploadDir = new File(Constant.DIR);
        if (!uploadDir.exists()) uploadDir.mkdirs();

        String videoId = req.getParameter("videoId");
        String title = req.getParameter("title");
        String description = req.getParameter("description");
        int active = req.getParameter("active") != null ? Integer.parseInt(req.getParameter("active")) : 1;

        Video video = ("/admin/video/update".equals(path)) ? videoService.findById(videoId) : new Video();
        if (video == null) video = new Video();

        video.setVideoId(videoId);
        video.setTitle(title);
        video.setDescription(description);
        video.setActive(active);

        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("account") != null) {
            video.setUser((User) session.getAttribute("account"));
        }

        Part posterPart = req.getPart("posterFile");
        if (posterPart != null && posterPart.getSize() > 0) {
            String rawName = new File(getFileName(posterPart)).getName();
            String savedPoster = System.currentTimeMillis() + "_" + rawName;
            posterPart.write(Constant.DIR + File.separator + savedPoster);
            video.setPoster(savedPoster);
        } else if (req.getParameter("posterUrl") != null && !req.getParameter("posterUrl").isEmpty()) {
            video.setPoster(req.getParameter("posterUrl"));
        }

        Part videoPart = req.getPart("videoFileUpload");
        if (videoPart != null && videoPart.getSize() > 0) {
            String rawVidName = new File(getFileName(videoPart)).getName();
            String savedVideo = System.currentTimeMillis() + "_" + rawVidName;
            videoPart.write(Constant.DIR + File.separator + savedVideo);
            video.setVideoFile(savedVideo);
        } else if (req.getParameter("videoUrl") != null && !req.getParameter("videoUrl").isEmpty()) {
            video.setVideoFile(req.getParameter("videoUrl"));
        }

        if ("/admin/video/insert".equals(path)) {
            videoService.insert(video);
        } else {
            videoService.update(video);
        }
        resp.sendRedirect(req.getContextPath() + "/admin/videos");
    }
}