package com.secretquest.ws.infrastructure.entities;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Table(name = "cards")
public class Card {
  @Id
  private int id;
  private int power;
}
