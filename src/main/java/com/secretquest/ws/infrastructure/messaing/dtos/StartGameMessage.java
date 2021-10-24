package com.secretquest.ws.infrastructure.messaing.dtos;

import com.secretquest.ws.infrastructure.messaing.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class StartGameMessage extends Message {

  protected String sessionId;
}
