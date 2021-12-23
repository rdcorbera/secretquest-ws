package com.secretquest.ws.business.usecases;

import com.secretquest.ws.business.exceptions.DeckPlayerNotFound;
import com.secretquest.ws.business.models.Deck;
import com.secretquest.ws.business.models.Game;
import com.secretquest.ws.business.ports.DeckServicePort;
import org.springframework.stereotype.Component;

@Component
public class AssignDecksUseCase {

  private final DeckServicePort deckServicePort;

  public AssignDecksUseCase(DeckServicePort deckServicePort) {
    this.deckServicePort = deckServicePort;
  }

  public void execute(Game game) throws DeckPlayerNotFound {
    Deck deckPlayerOne = deckServicePort.findDecks(game.getPlayerOne()).stream().findFirst()
        .orElseThrow(() -> new DeckPlayerNotFound("Deck not found"));
    game.getPlayerOne().setDeck(deckPlayerOne);

    Deck deckPlayerTwo = deckServicePort.findDecks(game.getPlayerTwo()).stream().findFirst()
        .orElseThrow(() -> new DeckPlayerNotFound("Deck not found"));
    game.getPlayerTwo().setDeck(deckPlayerTwo);
  }
}
