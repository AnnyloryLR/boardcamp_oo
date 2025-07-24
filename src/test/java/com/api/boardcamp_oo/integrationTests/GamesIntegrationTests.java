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

import com.api.boardcamp_oo.dtos.GamesDTO;
import com.api.boardcamp_oo.models.GamesModel;
import com.api.boardcamp_oo.repositories.GamesRepository;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class GamesIntegrationTests {
    
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private GamesRepository gamesRepository;

    @BeforeEach
    void cleanUpDatabase(){
        gamesRepository.deleteAll();
    }

    @Test
    void givenGameWithSameName_whenCreatingGame_thenThrowError(){

        GamesModel game = new GamesModel(null,"name",
            "image", 5, 1000);
        gamesRepository.save(game);

        GamesDTO newGame = new GamesDTO("name", 
            "image", 5, 1000);
        
        HttpEntity<GamesDTO> body = new HttpEntity<>(newGame);
        

        ResponseEntity<String> response = restTemplate.exchange(
            "/games", 
            HttpMethod.POST,
            body,
            String.class
        );

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("a game with that name already exists.", response.getBody());
        assertEquals(1, gamesRepository.count());
    }

    @Test
    void givenValidGame_whenCreatingGame_thenCreateGame(){
        
        GamesDTO newGame = new GamesDTO("name", 
            "image", 5, 1000);
        
        HttpEntity<GamesDTO> body = new HttpEntity<>(newGame);

        ResponseEntity<GamesModel> response = restTemplate.exchange(
            "/games", 
            HttpMethod.POST,
            body,
            GamesModel.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newGame.getName(), response.getBody().getName());
        assertEquals(newGame.getPricePerDay(), response.getBody().getPricePerDay());
        assertEquals(1, gamesRepository.count());
    }

}
