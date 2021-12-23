package com.secretquest.ws.infrastructure.entities;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "deck_cards")
public class DeckCard {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @ManyToOne()
  private Deck deck;
  @ManyToOne()
  private Card card;
}
