package com.api.boardcamp_oo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.boardcamp_oo.models.GamesModel;

@Repository
public interface GamesRepository extends JpaRepository<GamesModel, Long> {
    boolean existsByName(String name);
} 
