package com.api.boardcamp_oo.unitTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.api.boardcamp_oo.dtos.CustomersDTO;
import com.api.boardcamp_oo.errors.CustomerCpfConflictError;
import com.api.boardcamp_oo.errors.CustomerNotFoundError;
import com.api.boardcamp_oo.models.CustomersModel;
import com.api.boardcamp_oo.repositories.CustomersRepository;
import com.api.boardcamp_oo.services.CustomersService;

@SpringBootTest
class CustomersUnitTests {

    @InjectMocks
    private CustomersService customersService;

    @Mock
    private CustomersRepository customersRepository;

    @Test
    void givenNonExistingId_whenGettingCustomerById_thenThrowError(){
        Long id = 5L;
        doReturn(Optional.empty()).when(customersRepository).findById(any());

        CustomerNotFoundError error = assertThrows(
            CustomerNotFoundError.class,
            () -> customersService.getCustomerById(id));

        verify(customersRepository, times(1)).findById(any());
        verify(customersRepository, times(0)).save(any());
        assertNotNull(error);
        assertEquals("customer was not found!", error.getMessage());
    }

    @Test
    void givenExistingCpf_whenCreatingCustomer_thenThrowError(){

        CustomersDTO customer = new CustomersDTO("name", "phone", "cpf");
        doReturn(true).when(customersRepository).existsByCpf(any());

        CustomerCpfConflictError error = assertThrows(
            CustomerCpfConflictError.class,
            () -> customersService.createCustomer(customer));

        verify(customersRepository, times(1)).existsByCpf(any());
        verify(customersRepository, times(0)).save(any());
        assertNotNull(error);
        assertEquals("a customer with this cpf already exists.", error.getMessage());
    }

    @Test
    void givenValidCustomer_whenCreatingCustomer_thenCreateCustomer(){
        
        CustomersDTO customer = new CustomersDTO("name", "phone", "cpf");
        CustomersModel newCustomer = new CustomersModel(customer);
        doReturn(false).when(customersRepository).existsByCpf(any());
        doReturn(newCustomer).when(customersRepository).save(any());

        CustomersModel result = customersService.createCustomer(customer);

        verify(customersRepository, times(1)).existsByCpf(any());
        verify(customersRepository, times(1)).save(any());
        assertEquals(newCustomer, result);        
    }
}
