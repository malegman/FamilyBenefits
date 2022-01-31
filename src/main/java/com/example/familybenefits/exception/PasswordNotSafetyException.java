package com.example.familybenefits.exception;

/**
 * Исключение, связанное с не соответствием паролем политики безопасности пароля
 */
public class PasswordNotSafetyException extends Exception {

  /**
   * Конструктор, создает исключение с описанием исключения
   * @param message описание исключения
   */
  public PasswordNotSafetyException(String message) {
    super(message);
  }
}
