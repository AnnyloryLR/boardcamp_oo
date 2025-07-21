package com.api.boardcamp_oo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.boardcamp_oo.dtos.GamesDTO;
import com.api.boardcamp_oo.models.GamesModel;
import com.api.boardcamp_oo.services.GamesService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/games")
public class GamesController {
    
    final GamesService gamesService;

    GamesController(GamesService gamesService){
        this.gamesService = gamesService;
    }

    @GetMapping()
    public ResponseEntity<Object> getGames(){
        return ResponseEntity.status(HttpStatus.OK).body(gamesService.getGames());
    }

    @PostMapping()
    public ResponseEntity<GamesModel> createGame(@RequestBody @Valid GamesDTO body){
        GamesModel game = gamesService.createGame(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(game);
    }
}
