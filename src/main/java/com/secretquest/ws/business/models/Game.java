package com.secretquest.ws.business.models;

import lombok.Data;

@Data
public class Game {
  private Player playerOne;
  private Player playerTwo;
  private GameStatus status;
}
