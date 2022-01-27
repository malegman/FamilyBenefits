package com.example.familybenefits.exception;

/**
 * Исключение, которое сигнализирует, что пароль не соответствует политике безопасности пароля
 */
public class PasswordNotSafetyException extends Exception {

  /**
   * Конуструктор, создает исключение с описанием исключения
   * @param message описание исключения
   */
  public PasswordNotSafetyException(String message) {
    super(message);
  }
}
