package com.secretquest.ws.infrastructure.handlers;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;

@Service
public class SessionHandler {
  private List<WebSocketSession> sessions = new ArrayList<>();

  public List<WebSocketSession> getSessions() {
    return this.sessions;
  }
}
