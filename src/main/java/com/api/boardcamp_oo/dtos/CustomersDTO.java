package com.api.boardcamp_oo.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomersDTO {
    
    @NotBlank
    @Size(max=150)
    private String name;
    
    @NotBlank
    @Size(min=10, max=11)
    private String phone;
    
    @NotBlank
    @Size(min=11, max=11)
    private String cpf;

}
