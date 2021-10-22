package com.secretquest.ws;

import org.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class ServerWebSocketHandler extends TextWebSocketHandler {

  private List<WebSocketSession> sessions = new ArrayList<>();
  private Queue<String> messages = new ArrayDeque<>();

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
    String username = new JSONObject(request).getString("username");

    String response = String.format("Welcome '%s'", username);

    messages.add(response);
  }

  @Scheduled(fixedRate = 1000)
  void sendPeriodicMessages() throws IOException {

    if (messages.size() == 0) {
      return;
    }

    String newUsername = messages.poll();

    for (WebSocketSession session : sessions) {
      System.out.println("session " + session.getId());
      if (session.isOpen()) {
        session.sendMessage(new TextMessage(newUsername));
      }
    }
  }
}
