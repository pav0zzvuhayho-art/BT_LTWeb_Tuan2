package LTWeb.repository;

import java.util.List;
import LTWeb.models.Video;

public interface IVideoDao {
    void insert(Video video);
    void update(Video video);
    void delete(String videoId);
    Video findById(String videoId);
    List<Video> findAll();
    List<Video> findTop10Newest();
    List<Video> findPaginated(int page, int pageSize, Integer userId);
    int count(Integer userId);
}