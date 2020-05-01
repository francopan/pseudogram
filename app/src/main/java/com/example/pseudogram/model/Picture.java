package com.example.pseudogram.model;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Objects;

public class Picture implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String title;
    private String description;
    private String path;

    public Picture() {
    }

    public Picture(String title, String description, String path) {
        this.title = title;
        this.description = description;
        this.path = path;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Picture picture = (Picture) o;
        return id == picture.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
