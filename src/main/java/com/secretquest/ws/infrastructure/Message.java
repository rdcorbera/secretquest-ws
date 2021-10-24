package com.secretquest.ws.infrastructure;

import lombok.Data;

@Data
public class Message {
  private MessageType type;
  private String action;
  private String body;
}
