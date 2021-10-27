package com.secretquest.ws.infrastructure.repositories;

import com.secretquest.ws.business.models.Card;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class CardRepository {
  private List<Card> cards = Arrays.asList(
      Card.builder().id("1").power(50).build(),
      Card.builder().id("2").power(100).build(),
      Card.builder().id("3").power(150).build(),
      Card.builder().id("4").power(200).build(),
      Card.builder().id("5").power(250).build()
  );

  public Optional<Card> findById(String id) {
    return cards.stream().filter(c -> c.getId().equals(id)).findFirst();
  }
}
