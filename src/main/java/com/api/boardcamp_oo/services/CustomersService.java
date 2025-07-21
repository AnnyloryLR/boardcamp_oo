package com.api.boardcamp_oo.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.api.boardcamp_oo.dtos.CustomersDTO;
import com.api.boardcamp_oo.errors.CustomerCpfConflictError;
import com.api.boardcamp_oo.errors.CustomerNotFoundError;
import com.api.boardcamp_oo.models.CustomersModel;
import com.api.boardcamp_oo.repositories.CustomersRepository;

@Service
public class CustomersService {
    
    final CustomersRepository customersRepository;

    CustomersService(CustomersRepository customersRepository){
        this.customersRepository = customersRepository;
    }

    public List<CustomersModel> getCustomers(){
        return customersRepository.findAll();
    }

    public CustomersModel getCustomerById(Long id){
        CustomersModel customer = customersRepository.findById(id).orElseThrow(
            () -> new CustomerNotFoundError("customer not found!"));

        return customer;
    }

    public CustomersModel createCustomer(CustomersDTO body){
        if (customersRepository.existsByCpf(body.getCpf())){
            throw new CustomerCpfConflictError("a customer with this cpf already exists.");
        }

        CustomersModel customer = new CustomersModel(body);
        return customersRepository.save(customer);
    }
 
}
