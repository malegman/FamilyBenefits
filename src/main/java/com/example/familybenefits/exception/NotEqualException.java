package com.example.familybenefits.exception;

/**
 * Исключение, связанное с не эквивалентностью объектов
 */
public class NotEqualException extends Exception {

  /**
   * Конструктор, создает исключение с описанием исключения
   * @param message описание исключения
   */
  public NotEqualException(String message) {
    super(message);
  }
}
