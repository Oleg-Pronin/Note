package com.example.note.entity;

import java.util.Date;

public class Note {
    private String name;
    private String description;
    private Date createDate;

    public Note(String name, String description, Date createDate) {
        this.name = name;
        this.description = description;
        this.createDate = createDate;
    }

    public String getName() {
        return name;
    }

    public Note setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Note setDescription(String description) {
        this.description = description;
        return this;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Note setCreateDate(Date createDate) {
        this.createDate = createDate;
        return this;
    }
}
