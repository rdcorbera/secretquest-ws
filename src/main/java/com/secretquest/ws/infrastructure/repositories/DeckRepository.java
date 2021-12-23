package com.secretquest.ws.infrastructure.repositories;

import com.secretquest.ws.infrastructure.entities.Deck;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeckRepository extends JpaRepository<Deck, Integer> {

  List<Deck> findByAccountId(String accountId);
}
