package com.eg.eclothing.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class BaseProduct extends BaseEntity{
    private String name;
    
    @Column(unique = true)
    private String category;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
