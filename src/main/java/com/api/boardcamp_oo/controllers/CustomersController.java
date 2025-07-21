package com.api.boardcamp_oo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.boardcamp_oo.dtos.CustomersDTO;
import com.api.boardcamp_oo.models.CustomersModel;
import com.api.boardcamp_oo.services.CustomersService;
import com.api.boardcamp_oo.services.GamesService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/customers")
public class CustomersController {

    private final GamesService gamesService;

    final CustomersService customersService;

    public CustomersController(CustomersService customersService, GamesService gamesService){
        this.customersService = customersService;
        this.gamesService = gamesService;
    }
    
    @GetMapping()
    public ResponseEntity<Object> getCustomers(){
        return ResponseEntity.status(HttpStatus.OK).body(gamesService.getGames());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getCustomerById(@PathVariable Long id){
        CustomersModel customer = customersService.getCustomerById(id);
        return ResponseEntity.status(HttpStatus.OK).body(customer);
    }
    

    @PostMapping()
    public ResponseEntity<CustomersModel> createCustomer(@RequestBody @Valid CustomersDTO body){
        CustomersModel customer = customersService.createCustomer(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }

}
