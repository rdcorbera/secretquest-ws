package com.secretquest.ws.infrastructure.repositories;

import com.secretquest.ws.infrastructure.entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface CardRepository extends JpaRepository<Card, Integer> {
}
