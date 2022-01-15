package com.example.familybenefits.exception;

import org.springframework.lang.NonNull;

/**
 * Исключение, которое сигнализирует, что объект не был найден в базе данных
 */
public class NotFoundException extends Exception {

  /**
   * Конуструктор, создает исключение с описанием исключения
   * @param message описание исключения
   */
  public NotFoundException(@NonNull String message) {
    super(message);
  }
}
