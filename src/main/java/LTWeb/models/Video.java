package LTWeb.models;

import java.io.Serializable;
import jakarta.persistence.*;

@Entity
@Table(name = "videos")
@NamedQuery(name = "Video.findAll", query = "SELECT v FROM Video v ORDER BY v.videoId DESC")
public class Video implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "VideoId", columnDefinition = "VARCHAR(50)")
    private String videoId;

    @Column(name = "Active")
    private int active;

    @Column(name = "Description", columnDefinition = "NVARCHAR(500) NULL")
    private String description;

    @Column(name = "Poster", columnDefinition = "NVARCHAR(500) NULL")
    private String poster;

    @Column(name = "VideoFile", columnDefinition = "NVARCHAR(500) NULL")
    private String videoFile;

    @Column(name = "Title", columnDefinition = "NVARCHAR(500) NULL")
    private String title;

    @Column(name = "Views")
    private int views;

    @ManyToOne
    @JoinColumn(name = "userid")
    private User user;

    public Video() {}

    public String getVideoId() { return videoId; }
    public void setVideoId(String videoId) { this.videoId = videoId; }
    public int getActive() { return active; }
    public void setActive(int active) { this.active = active; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPoster() { return poster; }
    public void setPoster(String poster) { this.poster = poster; }
    public String getVideoFile() { return videoFile; }
    public void setVideoFile(String videoFile) { this.videoFile = videoFile; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public int getViews() { return views; }
    public void setViews(int views) { this.views = views; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}