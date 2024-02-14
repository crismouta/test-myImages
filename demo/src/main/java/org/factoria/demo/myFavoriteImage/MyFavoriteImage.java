package org.factoria.demo.myFavoriteImage;

import jakarta.persistence.*;
import org.factoria.demo.imageUser.ImageUser;

import java.io.Serializable;
@Entity
public class MyFavoriteImage implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String description;
    private String url;
    @ManyToOne
    private ImageUser owner;

    public MyFavoriteImage() {
    }
    public ImageUser getOwner() {
        return owner;
    }

    public void setOwner(ImageUser owner) {
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static class MyFavoriteImageNotFoundException extends RuntimeException{
        public MyFavoriteImageNotFoundException(Long id) {
            super("Could not find image with id: " + id);
        }
    }
}
