package com.api.boardcamp_oo.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GamesDTO {
    
    @NotBlank
    @Size(max = 150)
    private String name;

    @NotBlank
    private String image;

    @NotNull
    @Min(1)
    private Integer stockTotal;

    @NotNull
    @Min(1)
    private Integer pricePerDay;

}