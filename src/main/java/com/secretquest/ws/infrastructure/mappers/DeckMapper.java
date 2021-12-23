package com.secretquest.ws.infrastructure.mappers;

import com.secretquest.ws.business.models.Card;
import com.secretquest.ws.business.models.Deck;
import com.secretquest.ws.business.models.DeckCard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface DeckMapper {
  Deck map(com.secretquest.ws.infrastructure.entities.Deck deck);
  Card map(com.secretquest.ws.infrastructure.entities.Card card);
  @Mappings({
      @Mapping(target="cardId", source="entity.card.id"),
      @Mapping(target="power", source="entity.card.power")
  })
  DeckCard map(com.secretquest.ws.infrastructure.entities.DeckCard entity);
}
