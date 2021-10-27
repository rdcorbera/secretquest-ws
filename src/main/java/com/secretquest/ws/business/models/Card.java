package com.secretquest.ws.business.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Card {
  private String id;
  private int power;
}
