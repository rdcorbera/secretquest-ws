package com.secretquest.ws.infrastructure.controllers.match;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.secretquest.ws.business.models.Game;
import com.secretquest.ws.business.models.Match;
import com.secretquest.ws.business.models.Player;
import com.secretquest.ws.infrastructure.messaing.Message;
import com.secretquest.ws.infrastructure.messaing.MessageType;
import com.secretquest.ws.infrastructure.messaing.PubSubHandler;
import com.secretquest.ws.infrastructure.repositories.CardRepository;
import com.secretquest.ws.infrastructure.repositories.GameRepository;
import com.secretquest.ws.infrastructure.repositories.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MatchController {

  private GameRepository gameRepository;
  private CardRepository cardRepository;
  private PubSubHandler pubSubHandler;

  @Autowired
  public MatchController(
      GameRepository gameRepository,
      CardRepository cardRepository,
      PubSubHandler pubSubHandler) {
    this.gameRepository = gameRepository;
    this.cardRepository = cardRepository;
    this.pubSubHandler = pubSubHandler;
  }

  public void playCard(PlayCardRequest request) throws ResourceNotFoundException, JsonProcessingException {
    Game game = gameRepository.findById(request.getGameId())
        .orElseThrow(() -> new ResourceNotFoundException("Game not found"));

    Match match = game.playMatch(
        Player.builder().accountId(request.getAccountId()).build(),
        cardRepository.findById(request.getCardId()).get()
    );

    ObjectMapper mapper = new ObjectMapper();

    if (match.isDone()) {
      Message message = new Message();
      message.setType(MessageType.NOTIFICATION);
      message.setAction("MATCH_COMPLETED");
      message.setBody(mapper.writeValueAsString(match));

      pubSubHandler.sendMessage(game.getId().toString(), message);
    }
  }
}
