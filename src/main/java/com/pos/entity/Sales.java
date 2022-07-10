package com.pos.entity;

import java.time.LocalDate;
import lombok.*;

/**
 * Class used to create a table of sales.
 */
@Builder
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Sales {
    private Integer salesNumber;
    private LocalDate dateProcessed;
    private Double profit;

}
