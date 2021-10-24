package com.secretquest.ws.business.models;

import lombok.Data;

import java.util.UUID;

@Data
public class Game {
  private UUID id;
  private Player playerOne;
  private Player playerTwo;
  private GameStatus status;

  public Game() {
    this.id = UUID.randomUUID();
  }

  public void addPlayer(Player player) {
    if (playerOne == null) {
      playerOne = player;
      status = GameStatus.WAITING_OPPONENTS;
    } else {
      playerTwo = player;
      status = GameStatus.COMPLETED;
    }
  }
}
