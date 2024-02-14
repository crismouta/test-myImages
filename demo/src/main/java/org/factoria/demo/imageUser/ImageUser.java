package org.factoria.demo.imageUser;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import org.factoria.demo.myFavoriteImage.MyFavoriteImage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ImageUser implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotEmpty(message = "username is required.")
    private String name;
    private String surname;
    private String email;
    @NotEmpty(message = "password is required.")
    private String password;
    private boolean enabled;
    @NotEmpty(message = "roles are required.")
    private String roles;
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "owner", fetch=FetchType.EAGER)
    private List<MyFavoriteImage> images = new ArrayList<>();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public ImageUser() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<MyFavoriteImage> getImages() {
        return images;
    }

    public void setImages(List<MyFavoriteImage> images) {
        this.images = images;
    }

    public void addImage(MyFavoriteImage image) {
        image.setOwner(this);
        this.images.add(image);
    }

    public Integer getNumberOfImages() {
        return this.images.size();
    }
}
