package com.pos.entity;

import static javax.persistence.GenerationType.SEQUENCE;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import java.io.Serial;

/**
 * Class used to make a table for products.
 */
@Table(
        name = "products",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "product_id_unique",
                        columnNames = "product_id"
                )
        }
)

@Entity
public class Product implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(
            name = "product_id",
            nullable = false,
            columnDefinition = "INT",
            unique = true
    )
    @SequenceGenerator(
            name = "product_sequence",
            sequenceName = "product_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "product_sequence"
    )
    private Integer id;

    @Column(
            name = "name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String name;

    @Column(
            name = "price",
            nullable = false
    )
    private Double price;

    @Column(
            name = "category",
            columnDefinition = "TEXT",
            nullable = false
    )
    private Category category;

    @Column(
            name = "expiration_date",
            columnDefinition = "DATE",
            nullable = false
    )
    private LocalDate expirationDate;

    @Column(
            name = "stocks_left",
            columnDefinition = "INT",
            nullable = false
    )
    private Integer stocks;

    @Column(name = "discount")
    private Double discount;

    @Column(
            name = "expired",
            nullable = false,
            columnDefinition = "boolean"
    )
    private Boolean expired;

    public Product() {
    }

    public Product(String name, Double price, Category category, LocalDate expirationDate, Integer stocks, Double discount, Boolean expired) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.expirationDate = expirationDate;
        this.stocks = stocks;
        this.discount = discount;
        this.expired = expired;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Integer getStocks() {
        return stocks;
    }

    public void setStocks(Integer stocks) {
        this.stocks = stocks;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Boolean getExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }
    
    
}

