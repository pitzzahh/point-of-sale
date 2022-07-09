package com.pos.entity;

import com.pos.enums.Category;

/**
 * Class for making orders.
 * Not an entity.
 */
public final class Order {
    private String name;
    private double price;
    private Category category;
    private int quantity;
    private double discount;

    public Order(String name, double price, Category category, int quantity, double discount) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.quantity = quantity;
        this.discount = discount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
