package com.secretquest.ws.business.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class Game {
  private UUID id;
  private Player playerOne;
  private Player playerTwo;
  private GameStatus status;
  private List<Match> matches = new ArrayList<>();

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

  public Match playMatch(Player player, Card card) {
    Match match = matches.stream()
        .filter(m -> m.getStatus().equals(MatchStatus.WAITING_PLAYERS))
        .findFirst()
        .orElse(
            Match.builder()
                .playerOne(playerOne)
                .playerTwo(playerTwo)
                .index(matches.size() + 1)
                .status(MatchStatus.WAITING_PLAYERS)
                .build());

    matches.add(match);

    if (playerOne.getAccountId().equals(player.getAccountId())) {
      match.setCardPlayerOne(card);
    } else {
      match.setCardPlayerTwo(card);
    }

    if (match.isReadyForCombat()) {
      match.combat();
    }

    return match;
  }
}
