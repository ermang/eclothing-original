package com.eg.eclothing.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Category extends BaseEntity{

    @Column(unique = true, nullable = false)
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
