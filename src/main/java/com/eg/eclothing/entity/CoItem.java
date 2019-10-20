package com.eg.eclothing.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class CoItem extends BaseEntity {
    @NotNull
    @ManyToOne
    @JoinColumn(name = "create_co_id")
    private CreateCo createCo;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "stock_id")
    private Stock stock;

    public CreateCo getCreateCo() {
        return createCo;
    }

    public void setCreateCo(CreateCo createCo) {
        this.createCo = createCo;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }
}
