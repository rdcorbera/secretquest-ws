package com.secretquest.ws.infrastructure.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secretquest.ws.business.models.Game;
import com.secretquest.ws.business.models.GameStatus;
import com.secretquest.ws.business.models.Player;
import com.secretquest.ws.business.usecases.FindGameUseCase;
import com.secretquest.ws.infrastructure.handlers.SessionHandler;
import com.secretquest.ws.infrastructure.messaing.MessageType;
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

  private SessionHandler sessionHandler;
  private List<Game> games = new ArrayList<>();

  @Autowired
  public GameController(SessionHandler sessionHandler) {
    this.sessionHandler = sessionHandler;
  }

  public Game initGame(Player player) throws Exception {
    Game game = new FindGameUseCase().execute(games, player);

    if (games.indexOf(game) == -1) {
      games.add(game);
    }

    return game;
  }

  @Scheduled(fixedRate = 1000)
  void sendPeriodicMessages() throws IOException {
    if (games.size() == 0) {
      return;
    }

    ObjectMapper mapper = new ObjectMapper();

    for (WebSocketSession session : sessionHandler.getSessions()) {
      if (session.isOpen()) {
        for (Game game : games) {
          session.sendMessage(new TextMessage(mapper.writeValueAsString(game)));
        }
      }
    }
  }
}
