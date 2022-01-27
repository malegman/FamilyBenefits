package com.example.familybenefits.exception;

/**
 * Исключение, которое сигнализирует, что пароль и повторно введенный пароль не эквивалентны
 */
public class PasswordNotEqualsException extends Exception {

  /**
   * Конуструктор, создает исключение с описанием исключения
   * @param message описание исключения
   */
  public PasswordNotEqualsException(String message) {
    super(message);
  }
}
