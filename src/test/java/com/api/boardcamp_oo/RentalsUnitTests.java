package com.api.boardcamp_oo;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.api.boardcamp_oo.dtos.RentalsDTO;
import com.api.boardcamp_oo.errors.GameNotFoundError;
import com.api.boardcamp_oo.models.RentalsModel;
import com.api.boardcamp_oo.repositories.CustomersRepository;
import com.api.boardcamp_oo.repositories.GamesRepository;
import com.api.boardcamp_oo.repositories.RentalsRepository;
import com.api.boardcamp_oo.services.RentalsService;

@SpringBootTest
class RentalsUnitTests {

    @InjectMocks
    private RentalsService rentalsService;

    @Mock
    private RentalsRepository rentalsRepository;

    @Mock
    private CustomersRepository customersRepository;

    @Mock
    private GamesRepository gamesRepository;

    @Test
    void givenNonExistingGameId_whenCreatingRental_thenThrowError(){

        Long gameId = 3L;
        RentalsDTO rental = new RentalsDTO(LocalDate.now(),2,
         null, 3000, 0, 5L, 3L);
        doReturn(true).when(gamesRepository).findById(any());

        GameNotFoundError error = assertThrows(
            GameNotFoundError.class,
            () -> RentalsService.createRental(rental));





    }

    
}
