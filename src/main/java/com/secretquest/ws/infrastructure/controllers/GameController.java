package com.secretquest.ws.infrastructure.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secretquest.ws.business.models.Game;
import com.secretquest.ws.business.models.GameStatus;
import com.secretquest.ws.business.models.Player;
import com.secretquest.ws.business.usecases.FindGameUseCase;
import com.secretquest.ws.infrastructure.messaing.Message;
import com.secretquest.ws.infrastructure.messaing.MessageType;
import com.secretquest.ws.infrastructure.messaing.PubSubHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    String action = game.getStatus().equals(GameStatus.WAITING_OPPONENTS) ? "GAME_CREATED" : "GAME_COMPLETED";

    Message message = new Message();
    message.setType(MessageType.NOTIFICATION);
    message.setAction(action);
    message.setBody(mapper.writeValueAsString(game));

    pubSubHandler.sendMessage(game.getId().toString(), message);

    return game;
  }

  public void closeGame(String gameId) throws Exception
  {
    Game game = getGameById(gameId);
    if(game!=null)
    {
      ObjectMapper mapper = new ObjectMapper();
      Message message = new Message();
      message.setType(MessageType.NOTIFICATION);
      message.setAction("GAME_CLOSED");
      message.setBody(mapper.writeValueAsString(game));

      pubSubHandler.sendMessage(game.getId().toString(), message);
      pubSubHandler.removeTopic(gameId);
      games.remove(game);
    }
  }

  public Game getGameById(String gameId)
  {
    return games.stream().filter(s->s.getId().equals(gameId)).findFirst().get();
  }

  public void playCard(Player player, int cardId) throws  Exception{

  }
}
