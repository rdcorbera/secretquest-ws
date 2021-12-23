package com.secretquest.ws.infrastructure.entities;

import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Table(name = "decks")
public class Deck {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String accountId;
  private String name;
  @OneToMany(fetch = FetchType.EAGER)
  @JoinColumn(name = "deck_id")
  private List<DeckCard> cards;
}
