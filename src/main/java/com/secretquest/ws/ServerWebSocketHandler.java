package com.secretquest.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secretquest.ws.business.models.Player;
import com.secretquest.ws.infrastructure.Message;
import com.secretquest.ws.infrastructure.controllers.GameController;
import com.secretquest.ws.infrastructure.handlers.SessionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Component
public class ServerWebSocketHandler extends TextWebSocketHandler {

  @Autowired
  private SessionHandler sessionHandler;
  @Autowired
  private GameController gameController;

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    sessionHandler.getSessions().add(session);
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    WebSocketSession currentSession = sessionHandler.getSessions().stream()
        .filter(s -> session.getId().equals(s.getId()))
        .findFirst()
        .get();

    sessionHandler.getSessions().remove(currentSession);
  }

  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

    String request = message.getPayload();
    ObjectMapper mapper = new ObjectMapper();
    Message msg = mapper.readValue(request, Message.class);

    switch (msg.getAction()) {
      case "START_GAME":
        Player player = mapper.readValue(msg.getBody(), Player.class);
        gameController.initGame(player);
    }
  }
}
