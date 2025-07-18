package com.api.boardcamp_oo.services;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import com.api.boardcamp_oo.dtos.RentalsDTO;
import com.api.boardcamp_oo.errors.CustomerNotFoundError;
import com.api.boardcamp_oo.errors.GameNotFoundError;
import com.api.boardcamp_oo.errors.RentalBadRequestError;
import com.api.boardcamp_oo.errors.RentalNotFoundError;
import com.api.boardcamp_oo.errors.RentalUnprocessableEntityError;
import com.api.boardcamp_oo.models.CustomersModel;
import com.api.boardcamp_oo.models.GamesModel;
import com.api.boardcamp_oo.models.RentalsModel;
import com.api.boardcamp_oo.repositories.CustomersRepository;
import com.api.boardcamp_oo.repositories.GamesRepository;
import com.api.boardcamp_oo.repositories.RentalsRepository;

public class RentalsService {
    
    final RentalsRepository rentalsRepository;
    final CustomersRepository customersRepository;
    final GamesRepository gamesRepository;

    public RentalsService(RentalsRepository rentalsRepository, CustomersRepository
     customersRepository, GamesRepository gamesRepository){
        this.rentalsRepository = rentalsRepository;
        this.customersRepository = customersRepository;
        this.gamesRepository = gamesRepository; 
    }

    public List<RentalsModel> getRentals(){
        return rentalsRepository.findAll();
    }  

    public RentalsModel createRental(RentalsDTO body){
        GamesModel game = gamesRepository.findById(body.getGameId()).orElseThrow(
            ()-> new GameNotFoundError("game with the given id doesn't exist"));
        
        CustomersModel customer = customersRepository.findById(body.getCustomerId()).orElseThrow(
            ()-> new CustomerNotFoundError("customer with the given id could not be found!")
        );

        RentalsModel newRental = new RentalsModel(body, customer, game);
        return rentalsRepository.save(newRental);   
    }

    public RentalsModel finalizeRental(Long id){
        RentalsModel rental = rentalsRepository.findById(id).orElseThrow(
            () -> new RentalNotFoundError("rental with the ginven id was not found!")
        );

        if(rental.getReturnDate() != null){
            throw new RentalUnprocessableEntityError("This rental has already been completed");
        }

        RentalsModel completedRental = new RentalsModel();
            
            completedRental.setId(rental.getId());
            completedRental.setReturnDate(LocalDate.now());

            Period period = rental.getRentDate().until(completedRental.getReturnDate());
            
            completedRental.setDelayFee(period.getDays() * rental.getGame().getPricePerDay());

            return rentalsRepository.save(completedRental);
    }

    public void deleteRental(Long id){
        RentalsModel rental = rentalsRepository.findById(id).orElseThrow(
            () -> new RentalNotFoundError("rental with the ginven id was not found!")
        );

        if(rental.getReturnDate() == null){
            throw new RentalBadRequestError("this rental has not been completed yet");
        }

        rentalsRepository.deleteById(id);
    }
}
