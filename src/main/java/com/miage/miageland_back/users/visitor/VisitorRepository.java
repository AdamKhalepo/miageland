package com.miage.miageland_back.users.visitor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Long> {

        boolean existsByEmail(String email);

        Optional<Visitor> findByEmail(String visitorEmail);
}