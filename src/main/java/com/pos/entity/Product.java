package com.pos.entity;

import com.pos.enums.Category;
import java.time.LocalDate;
import lombok.*;


/**
 * Class used to make a table for products.
 */
@Builder
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product {
    private Integer id;
    private String name;
    private Double price;
    private Category category;
    private LocalDate expirationDate;
    private Integer stocks;
    private Double discount;

    public Product(String name, Double price, Category category, LocalDate expirationDate, Integer stocks, Double discount) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.expirationDate = expirationDate;
        this.stocks = stocks;
        this.discount = discount;
    }

}

