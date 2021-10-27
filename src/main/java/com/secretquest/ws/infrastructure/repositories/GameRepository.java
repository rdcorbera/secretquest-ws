package com.secretquest.ws.infrastructure.repositories;

import com.secretquest.ws.business.models.Game;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class GameRepository {
  private List<Game> games = new ArrayList<>();

  public List<Game> list() {
    return games;
  }

  public void save(Game game) {
    games.add(game);
  }

  public Optional<Game> findById(String id) {
    return games.stream().filter(g -> g.getId().equals(id)).findFirst();
  }
}
