package com.api.boardcamp_oo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.boardcamp_oo.dtos.RentalsDTO;
import com.api.boardcamp_oo.models.RentalsModel;
import com.api.boardcamp_oo.services.RentalsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/rentals")
public class RentalsController {
    
    final RentalsService rentalsService;

    public RentalsController(RentalsService rentalsService){
        this.rentalsService = rentalsService;
    }

    @GetMapping()
    public ResponseEntity<Object> getRentals(){
        return ResponseEntity.status(HttpStatus.OK).body(rentalsService.getRentals());
    }

    @PostMapping()
    public ResponseEntity<RentalsModel> createRental(@RequestBody @Valid RentalsDTO body){
        RentalsModel rental = rentalsService.createRental(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(rental);

    }

    @PostMapping("/{id}/return")
    public ResponseEntity<RentalsModel> finalizeRental(@PathVariable Long id){
        RentalsModel rental = rentalsService.finalizeRental(id);
        return ResponseEntity.status(HttpStatus.OK).body(rental);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteRental(@PathVariable Long id){
        rentalsService.deleteRental(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
