package com.example.familybenefits.exception;

import org.springframework.lang.NonNull;

/**
 * Исключение, которое сигнализирует, что объект уже существует в базе данных
 */
public class AlreadyExistsException extends Exception {

  /**
   * Конуструктор, создает исключение с описанием исключения
   * @param message описание исключения
   */
  public AlreadyExistsException(@NonNull String message) {
    super(message);
  }
}
