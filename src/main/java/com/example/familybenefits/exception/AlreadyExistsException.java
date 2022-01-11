package com.example.familybenefits.exception;

/**
 * Исключение, которое сигнализирует, что объект уже существует в базе данных
 */
public class AlreadyExistsException extends Exception {

  /**
   * Конуструктор, создает исключение с описанием исключения
   * @param message описание исключения
   */
  public AlreadyExistsException(String message) {
    super(message);
  }
}
