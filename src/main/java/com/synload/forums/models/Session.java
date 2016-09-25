package com.synload.forums.models;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.synload.framework.sql.Model;
import com.synload.framework.sql.annotations.*;

import java.sql.ResultSet;

/**
 * Created by Nathaniel on 9/25/2016.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "class")
@SQLTable(name = "Session Data Model", version = 0.1, description = "session data, for logins")
public class Session extends Model {
    public Session(ResultSet rs) {
        super(rs);
    }
    public Session(Object... data) {
        super(data);
    }

    @MediumIntegerColumn(length = 20, Key = true, AutoIncrement = true)
    public long id;

    @StringColumn(length = 256)
    public String key;

    @StringColumn(length = 15)
    public String ip;

    // Relations

    @LongBlobColumn()
    @HasOne(of=User.class, key="id")
    public String user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
