package com.api.boardcamp_oo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.api.boardcamp_oo.dtos.GamesDTO;
import com.api.boardcamp_oo.errors.GameNameConflictError;
import com.api.boardcamp_oo.repositories.GamesRepository;
import com.api.boardcamp_oo.services.GamesService;

@SpringBootTest
class GamesUnitTests {

    @InjectMocks
    private GamesService gamesService;

    @Mock
    private GamesRepository gamesRepository;
    
    @Test
    void givenGameWithSameName_whenCreatingGame_thenThrowError(){
      
        GamesDTO game = new GamesDTO("name", "image", 3, 1500); 

        doReturn(true).when(gamesRepository).existsByName(any());

        GameNameConflictError error = assertThrows(
            GameNameConflictError.class,
            () -> gamesService.createGame(game));
        
        verify(gamesRepository, times(1) ).existsByName(any());
        assertNotNull(error);
        assertEquals("a game with that name already exists.", error.getMessage());
        
    }
}
