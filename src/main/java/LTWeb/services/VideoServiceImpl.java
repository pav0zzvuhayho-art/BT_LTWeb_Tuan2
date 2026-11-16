package LTWeb.services;

import java.util.List;
import LTWeb.models.Video;
import LTWeb.repository.IVideoDao;
import LTWeb.repository.VideoDaoImpl;

public class VideoServiceImpl implements IVideoService {
    IVideoDao videoDao = new VideoDaoImpl();

    @Override
    public void insert(Video video) { videoDao.insert(video); }

    @Override
    public void update(Video video) { videoDao.update(video); }

    @Override
    public void delete(String videoId) { videoDao.delete(videoId); }

    @Override
    public Video findById(String videoId) { return videoDao.findById(videoId); }

    @Override
    public List<Video> findAll() { return videoDao.findAll(); }

    @Override
    public List<Video> findTop10Newest() { return videoDao.findTop10Newest(); }

    @Override
    public List<Video> findPaginated(int page, int pageSize, Integer userId) {
        return videoDao.findPaginated(page, pageSize, userId);
    }

    @Override
    public int count(Integer userId) { return videoDao.count(userId); }
}