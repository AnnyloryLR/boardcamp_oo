package com.api.boardcamp_oo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.api.boardcamp_oo.models.RentalsModel;

@Repository
public interface RentalsRepository extends JpaRepository<RentalsModel, Long> {
    @Query(
        nativeQuery = true,
        value = "SELECT * FROM rentals WHERE game_id = :gameId AND return_date IS NULL;"
    )
    List<RentalsModel> getOpenRentalsByGameId(Long gameId);
}    
