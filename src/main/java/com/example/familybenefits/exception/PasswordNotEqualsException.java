package com.example.familybenefits.exception;

/**
 * Исключение, связанное с не эквивалентностью паролей
 */
public class PasswordNotEqualsException extends Exception {

  /**
   * Конструктор, создает исключение с описанием исключения
   * @param message описание исключения
   */
  public PasswordNotEqualsException(String message) {
    super(message);
  }
}
