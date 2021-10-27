package com.secretquest.ws.infrastructure.controllers.match;

import lombok.Data;

@Data
public class PlayCardRequest {
  private String accountId;
  private String gameId;
  private String cardId;
}
