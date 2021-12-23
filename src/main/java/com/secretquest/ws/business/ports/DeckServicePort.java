package com.secretquest.ws.business.ports;

import com.secretquest.ws.business.models.Deck;
import com.secretquest.ws.business.models.Player;

import java.util.List;

public interface DeckServicePort {

  List<Deck> findDecks(Player player);
}
