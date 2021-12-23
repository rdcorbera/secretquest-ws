package com.secretquest.ws.infrastructure.adapters;

import com.secretquest.ws.business.models.Deck;
import com.secretquest.ws.business.models.Player;
import com.secretquest.ws.business.ports.DeckServicePort;
import com.secretquest.ws.infrastructure.mappers.DeckMapper;
import com.secretquest.ws.infrastructure.repositories.DeckRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeckServicePortImpl implements DeckServicePort {

  private final DeckRepository deckRepository;
  private DeckMapper deckMapper = Mappers.getMapper(DeckMapper.class);

  public DeckServicePortImpl(
      DeckRepository deckRepository) {
    this.deckRepository = deckRepository;
  }

  @Override
  public List<Deck> findDecks(Player player) {
    List<com.secretquest.ws.infrastructure.entities.Deck> decks = deckRepository.findByAccountId(player.getAccountId());
    return decks.stream().map(d -> deckMapper.map(d)).collect(Collectors.toList());
  }
}
