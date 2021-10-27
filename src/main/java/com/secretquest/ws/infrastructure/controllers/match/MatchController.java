package com.secretquest.ws.infrastructure.controllers.match;

import com.secretquest.ws.business.models.Game;
import com.secretquest.ws.infrastructure.repositories.GameRepository;
import com.secretquest.ws.infrastructure.repositories.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MatchController {

  private GameRepository gameRepository;

  @Autowired
  public MatchController(GameRepository gameRepository) {
    this.gameRepository = gameRepository;
  }

  public void playCard(PlayCardRequest request) throws ResourceNotFoundException {
    Game game = gameRepository.findById(request.getGameId())
        .orElseThrow(() -> new ResourceNotFoundException("Game not found"));
  }
}
