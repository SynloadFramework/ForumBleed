package com.synload.forums.models;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.synload.framework.sql.Model;
import com.synload.framework.sql.annotations.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;

/**
 * Created by Nathaniel on 9/25/2016.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "class")
@SQLTable(name = "User Data Model", version = 0.1, description = "user data")
public class User extends Model {
    public User(ResultSet rs) {
        super(rs);
    }
    public User(Object... data) {
        super(data);
    }

    @MediumIntegerColumn(length = 20, Key = true, AutoIncrement = true)
    public long id;

    @StringColumn(length = 128)
    public String name;

    @StringColumn(length = 512)
    private String password;

    @StringColumn(length = 128)
    private String email;

    @StringColumn(length = 128)
    public String avatar;

    // Relations

    @LongBlobColumn()
    @HasMany(of=Category.class, key="id")
    public String moderates;

    @LongBlobColumn()
    @HasMany(of=Thread.class, key="id")
    public String threads;

    @LongBlobColumn()
    @HasMany(of=Post.class, key="id")
    public String posts;

    @LongBlobColumn()
    @HasMany(of=Session.class, key="id")
    public String sessions;

    public static String hash(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());
        byte byteData[] = md.digest();
        // convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return sb.toString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getModerates() {
        return moderates;
    }

    public void setModerates(String moderates) {
        this.moderates = moderates;
    }

    public String getThreads() {
        return threads;
    }

    public void setThreads(String threads) {
        this.threads = threads;
    }

    public String getPosts() {
        return posts;
    }

    public void setPosts(String posts) {
        this.posts = posts;
    }

    public String getSessions() {
        return sessions;
    }

    public void setSessions(String sessions) {
        this.sessions = sessions;
    }
}
