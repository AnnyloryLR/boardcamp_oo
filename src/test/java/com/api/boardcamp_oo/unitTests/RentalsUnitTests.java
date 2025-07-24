package com.api.boardcamp_oo.unitTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.api.boardcamp_oo.dtos.CustomersDTO;
import com.api.boardcamp_oo.dtos.GamesDTO;
import com.api.boardcamp_oo.dtos.RentalsDTO;
import com.api.boardcamp_oo.errors.CustomerNotFoundError;
import com.api.boardcamp_oo.errors.GameNotFoundError;
import com.api.boardcamp_oo.errors.RentalUnprocessableEntityError;
import com.api.boardcamp_oo.models.CustomersModel;
import com.api.boardcamp_oo.models.GamesModel;
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

        RentalsDTO rental = new RentalsDTO(LocalDate.now(),2,
         null, 3000, 0, 5L, 3L);
        doReturn(Optional.empty()).when(gamesRepository).findById(any());

        GameNotFoundError error = assertThrows(
            GameNotFoundError.class,
            () -> rentalsService.createRental(rental));

        verify(gamesRepository, times(1)).findById(any());
        verify(rentalsRepository, times(0)).save(any());
        assertNotNull(error);
        assertEquals("game with the given id doesn't exist.", error.getMessage());
    }

    @Test
    void givenNonExistingCustomerId_whenCreatingRental_thenThrowError(){

        GamesDTO game = new GamesDTO("name", "image", 2, 1000);
        GamesModel existingGame = new GamesModel(game);
        RentalsDTO rental = new RentalsDTO(LocalDate.now(), 3, null,
         4500, 0, 1L, 2L);
        doReturn(Optional.of(existingGame)).when(gamesRepository).findById(any());
        doReturn(Optional.empty()).when(customersRepository).findById(any());

        CustomerNotFoundError error = assertThrows(
            CustomerNotFoundError.class,
            () -> rentalsService.createRental(rental));
        
        verify(customersRepository, times(1)).findById(any());
        verify(rentalsRepository, times(0)).save(any());
        assertNotNull(error);
        assertEquals("customer with the given id could not be found!", error.getMessage());
    }

    @Test
    void givenOpenRentalsEqualsToStockTotal_whenCreatingRental_thenThrowError(){
        
        GamesDTO game = new GamesDTO("name", "image", 2, 1000);
        GamesModel existingGame = new GamesModel(game);
        CustomersDTO customer = new CustomersDTO("name", "phone", "cpf");
        CustomersModel existingCustomer = new CustomersModel(customer);
        RentalsDTO rental = new RentalsDTO(LocalDate.now(), 3, null,
         4500, 0, 1L, 2L);
        List<RentalsModel> mockedList = mock(List.class);
        
        when(mockedList.size()).thenReturn(100);
        doReturn(Optional.of(existingGame)).when(gamesRepository).findById(any());
        doReturn(Optional.of(existingCustomer)).when(customersRepository).findById(any());
        doReturn(mockedList).when(rentalsRepository).getOpenRentalsByGameId(any());

        RentalUnprocessableEntityError error = assertThrows(
            RentalUnprocessableEntityError.class,
            () -> rentalsService.createRental(rental));
        
        verify(gamesRepository, times(1)).findById(any());
        verify(customersRepository, times(1)).findById(any());
        verify(rentalsRepository, times(0)).save(any());
        assertNotNull(error);
        assertEquals("no games with the given id are available.", error.getMessage());
    }

    @Test
    void givenValidRental_whenCreatingRental_thenCreateRental(){
        GamesDTO game = new GamesDTO("name", "image", 2, 1000);
        GamesModel existingGame = new GamesModel(game);
        CustomersDTO customer = new CustomersDTO("name", "phone", "cpf");
        CustomersModel existingCustomer = new CustomersModel(customer);
        RentalsDTO rental = new RentalsDTO(LocalDate.now(), 3, null,
         4500, 0, 1L, 2L);
        RentalsModel existingRental = new RentalsModel(rental, existingCustomer, existingGame);
        List<RentalsModel> mockedList = mock(List.class);
        
        when(mockedList.size()).thenReturn(0);
        doReturn(Optional.of(existingGame)).when(gamesRepository).findById(any());
        doReturn(Optional.of(existingCustomer)).when(customersRepository).findById(any());
        doReturn(mockedList).when(rentalsRepository).getOpenRentalsByGameId(any());
        doReturn(existingRental).when(rentalsRepository).save(any());

        RentalsModel result = rentalsService.createRental(rental);

        verify(gamesRepository, times(1)).findById(any());
        verify(customersRepository, times(1)).findById(any());
        verify(rentalsRepository, times(1)).save(any());
        assertEquals(existingRental, result);
    }
    
}
