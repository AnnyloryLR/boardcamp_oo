package com.api.boardcamp_oo.integrationTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.api.boardcamp_oo.dtos.RentalsDTO;
import com.api.boardcamp_oo.models.CustomersModel;
import com.api.boardcamp_oo.models.GamesModel;
import com.api.boardcamp_oo.models.RentalsModel;
import com.api.boardcamp_oo.repositories.CustomersRepository;
import com.api.boardcamp_oo.repositories.GamesRepository;
import com.api.boardcamp_oo.repositories.RentalsRepository;

@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class RentalsIntegrationTests {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    GamesRepository gamesRepository;

    @Autowired
    CustomersRepository customersRepository;

    @Autowired
    RentalsRepository rentalsRepository;

    @BeforeEach
    void cleanUpDatabase(){
        rentalsRepository.deleteAll();
        gamesRepository.deleteAll();
        customersRepository.deleteAll();
        
    }

    @Test
    void givenNonExistingGameId_whenCreatingRental_thenThrowError(){

        GamesModel game = new GamesModel(null, "War", "image",
            5, 1500 );
        GamesModel deletedGame = gamesRepository.save(game);
        gamesRepository.deleteById(deletedGame.getId());
        Long gameId = deletedGame.getId();
        RentalsDTO rentalDTO = new RentalsDTO(LocalDate.now(), 3,
            null, 4500, 0, 5L, gameId);
        HttpEntity<RentalsDTO> body = new HttpEntity<>(rentalDTO);

        ResponseEntity<String> response = restTemplate.exchange("/rentals",
            HttpMethod.POST,
            body,
            String.class
            );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("game with the given id could not be found.", response.getBody());
        assertEquals(0, rentalsRepository.count());
    }

    @Test
    void givenNonExistingCustomerId_whenCreatingRental_thenThrowError(){
        GamesModel game = new GamesModel(null, "War", "image",
            5, 1500 );
        GamesModel newGame = gamesRepository.save(game);
        CustomersModel customer = new CustomersModel(null, "Janis",
            "11996331290","11111111111");
        CustomersModel deletedCustomer = customersRepository.save(customer);
        customersRepository.deleteById(deletedCustomer.getId());
        RentalsDTO rentalDTO = new RentalsDTO(LocalDate.now(), 3,
            null, 4500, 0, 5L, newGame.getId());
        HttpEntity<RentalsDTO> body = new HttpEntity<>(rentalDTO);

        ResponseEntity<String> response = restTemplate.exchange(
            "/rentals",
            HttpMethod.POST,
            body,
            String.class
            );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("customer with the given id could not be found!", response.getBody());
        assertEquals(0, rentalsRepository.count());   
    }

    @Test
    void givenOpenRentalsEqualsToStockTotal_whenCreatingRental_thenThrowError(){
        GamesModel game = new GamesModel(null, "Xadrez", "image",
            1, 1500 );
        GamesModel newGame = gamesRepository.save(game);

        CustomersModel customer = new CustomersModel(null, "Adriana",
            "11996331290","55555555555");
        CustomersModel newCustomer = customersRepository.save(customer);

        RentalsDTO rentalDTO = new RentalsDTO(LocalDate.now(), 3, null,
         4500, 0, customer.getId(), game.getId());
        RentalsModel newRental = new RentalsModel( rentalDTO, customer, game);
        rentalsRepository.save(newRental);

        RentalsDTO newRentalDTO = new RentalsDTO(LocalDate.now(), 3,
            null, 1500, 0, newCustomer.getId(), newGame.getId());

        HttpEntity<RentalsDTO> body = new HttpEntity<>(newRentalDTO);

        ResponseEntity<String> response = restTemplate.exchange(
            "/rentals",
            HttpMethod.POST,
            body,
            String.class
        );
        
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertEquals("no games with the given id are available.", response.getBody());
        assertEquals(1, rentalsRepository.count());

    }

    @Test
    void givenValidRental_whenCreatingRental_thenCreateRental(){
        GamesModel game = new GamesModel(null, "Xadrez", "image",
            1, 1500 );
        GamesModel newGame = gamesRepository.save(game);

        CustomersModel customer = new CustomersModel(null, "Adriana",
            "11996331290","55555555555");
        CustomersModel newCustomer = customersRepository.save(customer);

        RentalsDTO newRentalDTO = new RentalsDTO(LocalDate.now(), 3,
            null, 1500, 0, newCustomer.getId(), newGame.getId());

        HttpEntity<RentalsDTO> body = new HttpEntity<>(newRentalDTO);

        ResponseEntity<RentalsModel> response = restTemplate.exchange(
            "/rentals",
            HttpMethod.POST,
            body,
            RentalsModel.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newRentalDTO.getCustomerId(), response.getBody().getCustomer().getId());
        assertEquals(newRentalDTO.getGameId(), response.getBody().getGame().getId());
        assertEquals(1, rentalsRepository.count());
    } 
}
