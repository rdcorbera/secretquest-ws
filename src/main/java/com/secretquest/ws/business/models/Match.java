package com.secretquest.ws.business.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Match {
  private int index;
  private Player playerOne;
  private Player playerTwo;
  private Card cardPlayerOne;
  private Card cardPlayerTwo;
  private Player win;
  private MatchStatus status;

  public boolean isReadyForCombat() {
    return cardPlayerOne != null && cardPlayerTwo != null;
  }

  public boolean isDone() {
    return status.equals(MatchStatus.COMPLETED_DRAW) || status.equals(MatchStatus.COMPLETED_WINNER);
  }

  public void combat() {
    if (cardPlayerOne.getPower() == cardPlayerTwo.getPower()) {
      status = MatchStatus.COMPLETED_DRAW;
      return;
    }

    win = cardPlayerOne.getPower() > cardPlayerOne.getPower() ? playerOne : playerTwo;
    status = MatchStatus.COMPLETED_WINNER;
  }
}
