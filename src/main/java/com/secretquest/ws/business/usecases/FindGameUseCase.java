package com.secretquest.ws.business.usecases;

import com.secretquest.ws.business.models.Game;
import com.secretquest.ws.business.models.GameStatus;
import com.secretquest.ws.business.models.Player;

import java.util.List;

public class FindGameUseCase {

  public Game execute(List<Game> games, Player player) {

    Game game = games.stream()
        .filter(g -> g.getStatus().equals(GameStatus.WAITING_OPPONENTS))
        .findAny()
        .orElse(new Game());

    game.addPlayer(player);

    return  game;
  }
}
