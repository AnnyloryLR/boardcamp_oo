package com.api.boardcamp_oo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.boardcamp_oo.models.RentalsModel;

@Repository
public interface RentalsRepository extends JpaRepository<RentalsModel, Long> {
    
}
