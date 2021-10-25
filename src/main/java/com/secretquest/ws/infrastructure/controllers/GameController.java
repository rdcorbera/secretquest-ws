package com.secretquest.ws.infrastructure.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secretquest.ws.business.models.Game;
import com.secretquest.ws.business.models.GameStatus;
import com.secretquest.ws.business.models.Player;
import com.secretquest.ws.business.usecases.FindGameUseCase;
import com.secretquest.ws.infrastructure.handlers.SessionHandler;
import com.secretquest.ws.infrastructure.messaing.Message;
import com.secretquest.ws.infrastructure.messaing.MessageType;
import com.secretquest.ws.infrastructure.messaing.PubSubHandler;
import com.secretquest.ws.infrastructure.messaing.dtos.StartGameMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class GameController {

  private PubSubHandler pubSubHandler;
  private List<Game> games = new ArrayList<>();

  @Autowired
  public GameController(PubSubHandler pubSubHandler) {
    this.pubSubHandler = pubSubHandler;
  }

  public Game initGame(String sessionId, Player player) throws Exception {
    Game game = new FindGameUseCase().execute(games, player);

    if (!games.contains(game)) {
      games.add(game);
      pubSubHandler.createNewTopic(game.getId().toString());
    }

    pubSubHandler.addSubscriber(game.getId().toString(), sessionId);

    ObjectMapper mapper = new ObjectMapper();

    Message message = new Message();
    message.setType(MessageType.NOTIFICATION);
    message.setAction("GAME_CREATED");
    message.setBody(mapper.writeValueAsString(game));

    pubSubHandler.sendMessage(game.getId().toString(), message);

    return game;
  }
}
