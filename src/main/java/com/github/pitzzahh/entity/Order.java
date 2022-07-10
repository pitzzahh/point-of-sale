package com.github.pitzzahh.entity;

import com.github.pitzzahh.enums.Category;
import lombok.*;

/**
 * Class for making orders.
 * Not an entity.
 */
@Builder
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Order {
    private String name;
    private double price;
    private Category category;
    private int quantity;
    private double discount;
}
