package com.synload.forums.models;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.synload.framework.sql.Model;
import com.synload.framework.sql.annotations.*;

import java.sql.ResultSet;

/**
 * Created by Nathaniel on 9/25/2016.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "class")
@SQLTable(name = "Forum Data Model", version = 0.1, description = "data for forum (default forum id=1)")
public class Forum extends Model {
    public Forum(ResultSet rs) {
        super(rs);
    }
    public Forum(Object... data) {
        super(data);
    }

    @MediumIntegerColumn(length = 20, Key = true, AutoIncrement = true)
    public long id;

    @MediumIntegerColumn(length=5)
    public int order;

    @StringColumn(length = 128)
    public String title;

    @StringColumn(length = 128)
    public String uri;

    // Relations

    @LongBlobColumn()
    @HasMany(of=Category.class, key="id")
    public String category;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
}
