package com.secretquest.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secretquest.ws.business.models.Game;
import com.secretquest.ws.business.models.Player;
import com.secretquest.ws.infrastructure.controllers.match.MatchController;
import com.secretquest.ws.infrastructure.controllers.match.PlayCardRequest;
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
  @Autowired
  private MatchController matchController;

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
    Message msg = mapper.readValue(payload, Message.class);

    switch (msg.getAction()) {
      case "START_GAME":
        Player player = mapper.readValue(msg.getBody(), Player.class);
        gameController.initGame(session.getId(), player);
        break;
      case "PLAY_CARD":
        PlayCardRequest request = mapper.readValue(msg.getBody(), PlayCardRequest.class);
        matchController.playCard(request);
        break;
    }
  }
}
