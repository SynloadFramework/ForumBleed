package com.synload.forums.models;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.synload.framework.sql.Model;
import com.synload.framework.sql.annotations.*;

import java.sql.ResultSet;

/**
 * Created by Nathaniel on 9/25/2016.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "class")
@SQLTable(name = "Thread Data Model", version = 0.1, description = "data containing post, inside a category")
public class Thread extends Model {
    public Thread(ResultSet rs) {
        super(rs);
    }
    public Thread(Object... data) {
        super(data);
    }

    @StringColumn(length = 128)
    public String title;

    @StringColumn(length = 256)
    public String description;

    @StringColumn(length = 128)
    public String uri;

    @MediumIntegerColumn(length=40)
    public long created;

    @MediumIntegerColumn(length=40)
    public long updated;

    // Relations

    @LongBlobColumn()
    @HasOne(of=Category.class, key="id")
    private String category;

    @LongBlobColumn()
    @HasOne(of=User.class, key="id")
    private String user;

    @LongBlobColumn()
    @HasMany(of=Post.class, key="id")
    private String posts;

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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPosts() {
        return posts;
    }

    public void setPosts(String posts) {
        this.posts = posts;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getUpdated() {
        return updated;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }
}
