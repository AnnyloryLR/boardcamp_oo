package com.api.boardcamp_oo.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.api.boardcamp_oo.dtos.GamesDTO;
import com.api.boardcamp_oo.errors.GameNameConflictError;
import com.api.boardcamp_oo.models.GamesModel;
import com.api.boardcamp_oo.repositories.GamesRepository;

@Service
public class GamesService {
    
    final GamesRepository gamesRepository;

    GamesService(GamesRepository gamesRepository){
        this.gamesRepository = gamesRepository;
    }

    public List<GamesModel> getGames(){
        return gamesRepository.findAll();
    }

    public GamesModel createGame(GamesDTO body){
        if (gamesRepository.existsByName(body.getName())){
            throw new GameNameConflictError("a game with that name already exists.");
        }

        GamesModel game = new GamesModel(body);
        return gamesRepository.save(game);
    }  
}
