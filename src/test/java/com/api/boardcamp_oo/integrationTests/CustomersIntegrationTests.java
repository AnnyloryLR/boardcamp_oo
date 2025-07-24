package com.api.boardcamp_oo.integrationTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

import com.api.boardcamp_oo.dtos.CustomersDTO;
import com.api.boardcamp_oo.models.CustomersModel;
import com.api.boardcamp_oo.repositories.CustomersRepository;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CustomersIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CustomersRepository customersRepository;

    @BeforeEach
    void cleanUpDatabase(){
        customersRepository.deleteAll();
    }

    @Test
    void givenNonExistentId_whenGettingCustomerById_thenThrowError(){

        CustomersModel customer = new CustomersModel(null, "name", "phone", "cpf");
        CustomersModel deletedCustomer = customersRepository.save(customer);
        customersRepository.deleteById(deletedCustomer.getId());
        Long id = deletedCustomer.getId();

        ResponseEntity<String> response = restTemplate.getForEntity(
           "/customers/{id}",
           String.class,
           id 
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("customer was not found!", response.getBody());
        assertEquals(0, customersRepository.count());
    }

    @Test
    void givenSameCpf_whenCreatingCustomer_thenThrowError(){

        CustomersModel customer = new CustomersModel(null, "Alice", "11986531290", "11111111111");
        customersRepository.save(customer);

        CustomersDTO newCustomer = new CustomersDTO("Joana", "11996331290", "11111111111");

        HttpEntity<CustomersDTO> body = new HttpEntity<>(newCustomer);

        ResponseEntity<String> response = restTemplate.exchange(
            "/customers", 
            HttpMethod.POST,
            body,
            String.class
        );

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("a customer with this cpf already exists.", response.getBody());
        assertEquals(1, customersRepository.count());
    }
    @Test
    void givenValidCustomer_whenCreatingCustomer_thenCreateCustomer(){

        CustomersDTO newCustomer = new CustomersDTO("Joana", "11996331290", "33333333333");

        HttpEntity<CustomersDTO> body = new HttpEntity<>(newCustomer);

        ResponseEntity<CustomersModel> response = restTemplate.exchange(
            "/customers", 
            HttpMethod.POST,
            body,
            CustomersModel.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newCustomer.getName(), response.getBody().getName());
        assertEquals(newCustomer.getCpf(), response.getBody().getCpf());
        assertEquals(1, customersRepository.count());
    }
}

