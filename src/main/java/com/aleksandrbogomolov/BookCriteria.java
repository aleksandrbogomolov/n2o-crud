package com.aleksandrbogomolov;

import net.n2oapp.criteria.api.Criteria;

public class BookCriteria extends Criteria {
    private Integer id;
    private String name;
    private Integer authorId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
