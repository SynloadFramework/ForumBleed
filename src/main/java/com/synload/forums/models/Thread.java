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

    // Relations

    @LongBlobColumn()
    @HasOne(of=Forum.class, key="id")
    public String forum;

    @LongBlobColumn()
    @HasOne(of=User.class, key="id")
    public String user;

    @LongBlobColumn()
    @HasMany(of=Post.class, key="id")
    public String posts;

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

    public String getForum() {
        return forum;
    }

    public void setForum(String forum) {
        this.forum = forum;
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
}
