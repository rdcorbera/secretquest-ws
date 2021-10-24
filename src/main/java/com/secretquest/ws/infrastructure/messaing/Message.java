package com.secretquest.ws.infrastructure.messaing;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data
@EqualsAndHashCode
@Getter
public class Message {
  protected MessageType type;
  protected String action;
  protected String body;
}
