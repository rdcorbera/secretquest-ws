package com.secretquest.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secretquest.ws.business.models.Game;
import com.secretquest.ws.business.models.Player;
import com.secretquest.ws.infrastructure.messaing.Message;
import com.secretquest.ws.infrastructure.controllers.GameController;
import com.secretquest.ws.infrastructure.handlers.SessionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

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

    String payload = message.getPayload();
    ObjectMapper mapper = new ObjectMapper();
    Message request = mapper.readValue(payload, Message.class);

    switch (request.getAction()) {
      case "START_GAME":
        Player player = mapper.readValue(request.getBody(), Player.class);
        Game game = gameController.initGame(session.getId(), player);
    }
  }
}
