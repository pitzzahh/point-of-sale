package com.pos.entity;

import static javax.persistence.GenerationType.SEQUENCE;
import java.io.Serializable;
import javax.persistence.*;
import java.time.LocalDate;
import java.io.Serial;

/**
 * Class used to create a table of sales.
 */
@Table(
        name = "sales",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "sales_number_unique",
                        columnNames = "sales_number"
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
            name = "date_processed",
            nullable = false,
            columnDefinition = "DATE"
    )
    private LocalDate dateProcessed;

    @Column(
            name = "profit",
            nullable = false
    )
    private Double profit;

    public Sales() {
    }

    public Sales(LocalDate date_processed, Double profit) {
        this.dateProcessed = date_processed;
        this.profit = profit;
    }

    public Integer getSalesNumber() {
        return salesNumber;
    }

    public LocalDate getDateProcessed() {
        return dateProcessed;
    }

    public void setDateProcessed(LocalDate dateProcessed) {
        this.dateProcessed = dateProcessed;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }
    
    
}
