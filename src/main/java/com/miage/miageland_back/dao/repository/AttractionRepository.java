package com.miage.miageland_back.dao.repository;

import com.miage.miageland_back.entities.Attraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttractionRepository extends JpaRepository<Attraction, Long> {

    boolean existsByName(String name);
}
