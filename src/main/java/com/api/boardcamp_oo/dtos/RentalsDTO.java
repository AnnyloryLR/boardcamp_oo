package com.api.boardcamp_oo.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RentalsDTO {
    
    private LocalDate rentDate;

    @NotNull
    @Min(1)
    private Integer daysRented;

    private LocalDate returnDate;

    private Integer originalPrice;

    private Integer delayFee;

    @NotNull
    private Long customerId;
    
    @NotNull    
    private Long gameId;

    public Long getCustomerId(){
        return customerId;
    }

    public Long getGameId(){
        return gameId;
    }


}
