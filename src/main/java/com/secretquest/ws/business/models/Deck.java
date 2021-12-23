package com.secretquest.ws.business.models;

import lombok.Data;

import java.util.List;

@Data
public class Deck {
  private String name;
  private List<DeckCard> cards;
}
