package com.synload.forums.models;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.synload.framework.sql.Model;
import com.synload.framework.sql.annotations.*;

import java.sql.ResultSet;

/**
 * Created by Nathaniel on 9/24/2016.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "class")
@SQLTable(name = "Post Data Model", version = 0.1, description = "post body and creator")
public class Post extends Model {
    public Post(ResultSet rs) {
        super(rs);
    }
    public Post(Object... data) {
        super(data);
    }

    @MediumIntegerColumn(length = 20, Key = true, AutoIncrement = true)
    public long id;

    @StringColumn(length = 128)
    public String subject;

    @LongBlobColumn()
    public String body;

    // Relations

    @LongBlobColumn()
    @HasOne(of=User.class, key="id")
    public String creator;

    @LongBlobColumn()
    @HasOne(of=Thread.class, key="id")
    public String thread;

    @LongBlobColumn()
    @HasMany(of=Post.class, key="id")
    public String posts;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getThread() {
        return thread;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }

    public String getPosts() {
        return posts;
    }

    public void setPosts(String posts) {
        this.posts = posts;
    }
}
