package com.secretquest.ws.infrastructure.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secretquest.ws.business.models.Game;
import com.secretquest.ws.business.models.GameStatus;
import com.secretquest.ws.business.models.Player;
import com.secretquest.ws.business.usecases.FindGameUseCase;
import com.secretquest.ws.infrastructure.messaing.Message;
import com.secretquest.ws.infrastructure.messaing.MessageType;
import com.secretquest.ws.infrastructure.messaing.PubSubHandler;
import com.secretquest.ws.infrastructure.repositories.GameRepository;
import com.secretquest.ws.infrastructure.repositories.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class GameController {

  private GameRepository gameRepository;
  private PubSubHandler pubSubHandler;

  @Autowired
  public GameController(GameRepository gameRepository, PubSubHandler pubSubHandler) {
    this.gameRepository = gameRepository;
    this.pubSubHandler = pubSubHandler;
  }

  public Game initGame(String sessionId, Player player) throws Exception {
    List<Game> games = gameRepository.list();
    Game game = new FindGameUseCase().execute(games, player);

    if (!games.contains(game)) {
      gameRepository.save(game);
      pubSubHandler.createNewTopic(game.getId().toString());
    }

    pubSubHandler.addSubscriber(game.getId().toString(), sessionId);

    ObjectMapper mapper = new ObjectMapper();
    String action = game.getStatus().equals(GameStatus.WAITING_OPPONENTS) ? "GAME_CREATED" : "GAME_COMPLETED";

    Message message = new Message();
    message.setType(MessageType.NOTIFICATION);
    message.setAction(action);
    message.setBody(mapper.writeValueAsString(game));

    pubSubHandler.sendMessage(game.getId().toString(), message);

    return game;
  }

  public void closeGame(String gameId) throws Exception {
    Optional<Game> gameResult = gameRepository.findById(gameId);

    if (gameResult.isPresent()) {
      Game game = gameResult.get();
      ObjectMapper mapper = new ObjectMapper();
      Message message = new Message();
      message.setType(MessageType.NOTIFICATION);
      message.setAction("GAME_CLOSED");
      message.setBody(mapper.writeValueAsString(game));

      pubSubHandler.sendMessage(game.getId().toString(), message);
      pubSubHandler.removeTopic(gameId);
      gameRepository.delete(game);
    }
  }
}
