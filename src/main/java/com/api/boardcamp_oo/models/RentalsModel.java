package com.api.boardcamp_oo.models;

import java.time.LocalDate;

import com.api.boardcamp_oo.dtos.RentalsDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rentals")
public class RentalsModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column( nullable = false)
    private LocalDate rentDate;

    @Column(nullable = false)
    private Integer daysRented;

    @Column(columnDefinition = "default null")
    private LocalDate returnDate;

    @Column(nullable = false)
    private Long originalPrice;

    @Column(columnDefinition = "default Integer 0")
    private Long delayFee;

    @ManyToOne
    @JoinColumn(name = "customerId")
    private CustomersModel customer;

    @ManyToMany
    @JoinTable(
        name = "rental-game",
        joinColumns = @JoinColumn(name = "rentalId"),
        inverseJoinColumns = @JoinColumn(name = "gameId")
    )
    private GamesModel game;

    public RentalsModel(RentalsDTO dto, CustomersModel customer, GamesModel game){
        this.rentDate = dto.getRentDate();
        this.daysRented = dto.getDaysRented();
        this.returnDate = dto.getReturnDate();
        this.originalPrice = dto.getOriginalPrice();
        this.delayFee = dto.getDelayFee();
        this.customer = customer;
        this.game = game;
    }
}
