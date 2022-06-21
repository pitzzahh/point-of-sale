package com.pos.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.io.Serial;

import static javax.persistence.GenerationType.SEQUENCE;

@Table(
        name = "sales",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "sales_number_unique",
                        columnNames = "sales_number"
                ),
                @UniqueConstraint(
                        name = "product_id_unique",
                        columnNames = "product_id"
                )
        }
)
@Entity
public class Sales implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(
            name = "sales_number",
            nullable = false,
            columnDefinition = "INT",
            unique = true
    )
    @SequenceGenerator(
            name = "sales_sequence",
            sequenceName = "sales_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "sales_sequence"
    )
    private Integer salesNumber;

    @Column(
            name = "product_id",
            nullable = false,
            columnDefinition = "INT",
            unique = true
    )
    private Integer productId;

    @Column(
            name = "product_name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String productName;

    @Column(
            name = "sales",
            nullable = false
    )
    private Double sales;

    public Sales() {
    }

    public Sales(Integer productId, String productName, Double sales) {
        this.productId = productId;
        this.productName = productName;
        this.sales = sales;
    }

    public Integer getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getSales() {
        return sales;
    }

    public void setSales(Double sales) {
        this.sales = sales;
    }
}
