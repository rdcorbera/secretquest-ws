package com.secretquest.ws.infrastructure.controllers;

import com.secretquest.ws.business.models.Game;
import com.secretquest.ws.business.models.GameStatus;
import com.secretquest.ws.business.models.Player;
import com.secretquest.ws.infrastructure.handlers.SessionHandler;
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

  public void create(Player player) {
    Game newGame = new Game();
    newGame.setPlayerOne(player);
    newGame.setStatus(GameStatus.WAITING_OPPONENTS);

    games.add(newGame);
  }

  @Scheduled(fixedRate = 1000)
  void sendPeriodicMessages() throws IOException {
    if (games.size() == 0) {
      return;
    }

    for (WebSocketSession session : sessionHandler.getSessions()) {
      if (session.isOpen()) {
        session.sendMessage(new TextMessage(games.get(0).toString()));
      }
    }
  }
}
