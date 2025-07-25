package com.api.boardcamp_oo.services;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import org.springframework.stereotype.Service;

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

@Service
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
            ()-> new GameNotFoundError("game with the given id could not be found."));
        
        CustomersModel customer = customersRepository.findById(body.getCustomerId()).orElseThrow(
            ()-> new CustomerNotFoundError("customer with the given id could not be found!")
        );

        List<RentalsModel> openRentals = rentalsRepository.getOpenRentalsByGameId(game.getId());
        Integer gamesWithOpenRental = openRentals.size();

        if(gamesWithOpenRental >= game.getStockTotal()){
            throw new RentalUnprocessableEntityError("no games with the given id are available.");
        }

        Integer priceTotal = game.getPricePerDay() * body.getDaysRented();

        RentalsModel newRental = new RentalsModel(body, customer, game);
            newRental.setRentDate(LocalDate.now());
            newRental.setReturnDate(null);
            newRental.setOriginalPrice(priceTotal);
            newRental.setDelayFee(0);
            newRental.setCustomer(customer);
            newRental.setGame(game);
        return rentalsRepository.save(newRental);   
    }

    public RentalsModel finalizeRental(Long id){
        RentalsModel rental = rentalsRepository.findById(id).orElseThrow(
            () -> new RentalNotFoundError("rental with the ginven id was not found!")
        );

        if(rental.getReturnDate() != null){
            throw new RentalUnprocessableEntityError("This rental has already been completed.");
        }

         Period period = rental.getRentDate().until(LocalDate.now());
         Integer feeForDelay = (period.getDays() - rental.getDaysRented()) * rental.getGame().getPricePerDay();

        RentalsModel completedRental = new RentalsModel();
            completedRental.setId(rental.getId());
            completedRental.setRentDate(rental.getRentDate());
            completedRental.setDaysRented(rental.getDaysRented());
            completedRental.setReturnDate(LocalDate.now());
            completedRental.setOriginalPrice(rental.getOriginalPrice());
            completedRental.setDelayFee(feeForDelay);
            completedRental.setCustomer(rental.getCustomer());
            completedRental.setGame(rental.getGame());

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
