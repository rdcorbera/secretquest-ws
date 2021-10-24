package com.secretquest.ws;

import com.secretquest.ws.infrastructure.controllers.GameController;
import com.secretquest.ws.infrastructure.handlers.SessionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;


@Configuration
@EnableScheduling
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(webSocketHandler(), "/websocket");
  }

  @Bean
  public WebSocketHandler webSocketHandler() {
    /**
     * Revisar como manejar esto de la mejor manera considerando la inyección de dependencias de Spring boot
     * Pareciera que se estan creando 2 instancias de ServerWebSocketHandler
     * Esto se ve cuando se incluye un método de Job en esa clase
     */
    return new ServerWebSocketHandler();
  }
}
