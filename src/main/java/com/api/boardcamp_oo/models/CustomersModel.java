package com.api.boardcamp_oo.models;

import com.api.boardcamp_oo.dtos.CustomersDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customers" )
public class CustomersModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 150, nullable = false)
    private String name;

    @Column(length = 11, nullable = false)
    private String phone;

    @Column(length = 11, nullable = false, unique = true)
    private String cpf;

    public CustomersModel(CustomersDTO dto){
        this.name = dto.getName();
        this.phone = dto.getPhone();
        this.cpf = dto.getCpf();
    }
}
