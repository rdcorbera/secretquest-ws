package com.secretquest.ws.infrastructure.repositories;

public class ResourceNotFoundException extends Exception {

  public ResourceNotFoundException(String message) {
    super(message);
  }
}
