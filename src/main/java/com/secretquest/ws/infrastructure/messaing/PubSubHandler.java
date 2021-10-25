package com.secretquest.ws.infrastructure.messaing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import com.secretquest.ws.infrastructure.handlers.SessionHandler;
import com.secretquest.ws.infrastructure.messaing.dtos.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PubSubHandler {

  private SessionHandler sessionHandler;

  @Autowired
  public PubSubHandler(SessionHandler sessionHandler) {
    this.sessionHandler = sessionHandler;
  }

  private List<Topic> topics = new ArrayList<>();

  public void createNewTopic(String topicId) {
    Topic newTopic = new Topic(topicId);
    topics.add(newTopic);
  }

  public void addSubscriber(String topicId, String subscriberId) {
    Topic topic = getTopicById(topicId);

    topic.addSubscriber(subscriberId);
  }

  public void sendMessage(String topicId, Message message) {
    Topic topic = getTopicById(topicId);

    ObjectMapper mapper = new ObjectMapper();

    topic.getSubscribers().forEach(s -> {
      WebSocketSession session = sessionHandler.getSessionById(s.getId());
      try {
        session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

  private Topic getTopicById(String topicId) {
    return topics.stream().filter(t -> t.getTopicId().equals(topicId)).findFirst().get();
  }
}
