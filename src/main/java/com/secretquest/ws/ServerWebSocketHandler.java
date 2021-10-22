package com.secretquest.ws;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ServerWebSocketHandler extends TextWebSocketHandler {

  private List<WebSocketSession> sessions = new ArrayList<>();

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    sessions.add(session);
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    WebSocketSession currentSession = sessions.stream()
        .filter(s -> session.getId().equals(s.getId()))
        .findFirst()
        .get();

    sessions.remove(currentSession);
  }

  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

    String request = message.getPayload();

    String response = String.format("response from server to '%s'", HtmlUtils.htmlEscape(request));

    session.sendMessage(new TextMessage(response));
  }

  @Scheduled(fixedRate = 1000)
  void sendPeriodicMessages() throws IOException {
    for (WebSocketSession session : sessions) {
      System.out.println("session " + session.getId());
      if (session.isOpen()) {
        String broadcast = "server periodic message " + LocalTime.now();

        session.sendMessage(new TextMessage(broadcast));
      }
    }
  }
}
