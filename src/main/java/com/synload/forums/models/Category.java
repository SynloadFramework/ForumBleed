package com.synload.forums.models;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.synload.framework.sql.Model;
import com.synload.framework.sql.annotations.*;

import java.sql.ResultSet;

/**
 * Created by Nathaniel on 9/24/2016.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "class")
@SQLTable(name = "Category Data Model", version = 0.1, description = "categories for the forum")
public class Category extends Model {
    public Category(ResultSet rs) {
        super(rs);
    }
    public Category(Object... data) {
        super(data);
    }

    @MediumIntegerColumn(length = 20, Key = true, AutoIncrement = true)
    public long id;

    @StringColumn(length = 128)
    public String title;

    @StringColumn(length = 256)
    public String description;

    @MediumIntegerColumn(length=5)
    public int order;

    @StringColumn(length = 128)
    public String uri;

    // Relations

    @LongBlobColumn()
    @HasOne(of=Forum.class, key="id")
    public String forum;

    @LongBlobColumn()
    @HasMany(of=User.class, key="id")
    public String moderators;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
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

    public String getModerators() {
        return moderators;
    }

    public void setModerators(String moderators) {
        this.moderators = moderators;
    }
}
