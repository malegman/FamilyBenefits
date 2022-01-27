package com.example.familybenefits.exception;

/**
 * Исключение, которое сигнализирует, что email некорректный
 */
public class InvalidEmailException extends Exception {

  /**
   * Конуструктор, создает исключение с описанием исключения
   * @param message описание исключения
   */
  public InvalidEmailException(String message) {
    super(message);
  }
}
