package LTWeb.repository;

import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import LTWeb.configs.JPAConfig;
import LTWeb.models.Video;

public class VideoDaoImpl implements IVideoDao {

    @Override
    public void insert(Video video) {
        EntityManager enma = JPAConfig.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            enma.persist(video);
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (trans.isActive()) trans.rollback();
        } finally {
            enma.close();
        }
    }

    @Override
    public void update(Video video) {
        EntityManager enma = JPAConfig.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            enma.merge(video);
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (trans.isActive()) trans.rollback();
        } finally {
            enma.close();
        }
    }

    @Override
    public void delete(String videoId) {
        EntityManager enma = JPAConfig.getEntityManager();
        EntityTransaction trans = enma.getTransaction();
        try {
            trans.begin();
            Video video = enma.find(Video.class, videoId);
            if (video != null) {
                enma.remove(video);
            }
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (trans.isActive()) trans.rollback();
        } finally {
            enma.close();
        }
    }

    @Override
    public Video findById(String videoId) {
        EntityManager enma = JPAConfig.getEntityManager();
        try {
            return enma.find(Video.class, videoId);
        } finally {
            enma.close();
        }
    }

    @Override
    public List<Video> findAll() {
        EntityManager enma = JPAConfig.getEntityManager();
        try {
            return enma.createNamedQuery("Video.findAll", Video.class).getResultList();
        } finally {
            enma.close();
        }
    }

    @Override
    public List<Video> findTop10Newest() {
        EntityManager enma = JPAConfig.getEntityManager();
        try {
            String jpql = "SELECT v FROM Video v WHERE v.active = 1 ORDER BY v.videoId DESC";
            TypedQuery<Video> query = enma.createQuery(jpql, Video.class);
            query.setMaxResults(10);
            return query.getResultList();
        } finally {
            enma.close();
        }
    }

    @Override
    public List<Video> findPaginated(int page, int pageSize, Integer userId) {
        EntityManager enma = JPAConfig.getEntityManager();
        try {
            String jpql = (userId != null)
                    ? "SELECT v FROM Video v WHERE v.user.id = :uid ORDER BY v.videoId DESC"
                    : "SELECT v FROM Video v ORDER BY v.videoId DESC";
            TypedQuery<Video> query = enma.createQuery(jpql, Video.class);
            if (userId != null) query.setParameter("uid", userId);
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);
            return query.getResultList();
        } finally {
            enma.close();
        }
    }

    @Override
    public int count(Integer userId) {
        EntityManager enma = JPAConfig.getEntityManager();
        try {
            String jpql = (userId != null)
                    ? "SELECT COUNT(v) FROM Video v WHERE v.user.id = :uid"
                    : "SELECT COUNT(v) FROM Video v";
            TypedQuery<Long> query = enma.createQuery(jpql, Long.class);
            if (userId != null) query.setParameter("uid", userId);
            return query.getSingleResult().intValue();
        } finally {
            enma.close();
        }
    }
}