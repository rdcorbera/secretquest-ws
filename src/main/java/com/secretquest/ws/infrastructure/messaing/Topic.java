package com.secretquest.ws.infrastructure.messaing;

import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
public class Topic {

  private String topicId;
  private List<Subscriber> subscribers = new ArrayList<>();

  public Topic(String topicId) {
    this.topicId = topicId;
  }

  public void addSubscriber(String subscriberId) {
    Subscriber subscriber = new Subscriber(subscriberId);
    this.subscribers.add(subscriber);
  }
}
