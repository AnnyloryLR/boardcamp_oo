package com.api.boardcamp_oo.unitTests;

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
import com.api.boardcamp_oo.models.GamesModel;
import com.api.boardcamp_oo.repositories.GamesRepository;
import com.api.boardcamp_oo.services.GamesService;

@SpringBootTest
class GamesUnitTests {

    @InjectMocks
    private GamesService gamesService;

    @Mock
    private GamesRepository gamesRepository;
    
    @Test
    void givenGameWithExistingName_whenCreatingGame_thenThrowError(){
      
        GamesDTO game = new GamesDTO("name", "image", 3, 1500); 
        doReturn(true).when(gamesRepository).existsByName(any());

        GameNameConflictError error = assertThrows(
            GameNameConflictError.class,
            () -> gamesService.createGame(game));
        
        verify(gamesRepository, times(1)).existsByName(any());
        verify(gamesRepository, times(0)).save(any());
        assertNotNull(error);
        assertEquals("a game with that name already exists.", error.getMessage()); 
    }

    @Test
    void givenValidGame_whenCreatingGame_thenCreateGame(){

        GamesDTO game = new GamesDTO("name", "image", 3, 1500);
        GamesModel newGame = new GamesModel(game);

        doReturn(false).when(gamesRepository).existsByName(any());
        doReturn(newGame).when(gamesRepository).save(any());

        GamesModel result = gamesService.createGame(game);

        verify(gamesRepository, times(1)).existsByName(any());
        verify(gamesRepository, times(1)).save(any());
        assertEquals(newGame, result);
    }
}
