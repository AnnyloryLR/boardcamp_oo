package com.api.boardcamp_oo.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class RentalsDTO {
    
    @NotNull
    private LocalDate renDate;

    @NotNull
    @Min(1)
    private Integer daysRented;

    private LocalDate returnDate;

    @NotNull
    private Long originalPrice;

    @PositiveOrZero
    private Long delayFee;
}
