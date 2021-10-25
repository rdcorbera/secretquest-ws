package com.secretquest.ws.infrastructure.messaing;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class Subscriber {
  private String id;

  public Subscriber(String id) {
    this.id = id;
  }
}
