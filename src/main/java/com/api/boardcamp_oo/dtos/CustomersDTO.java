package com.api.boardcamp_oo.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomersDTO {
    
    @NotBlank
    @Size(max=150)
    private String name;
    
    @NotNull
    @Max(11)
    @Min(10)
    private Integer phone;
    
    @NotBlank
    @Size(max=11)
    private String cpf;

}
