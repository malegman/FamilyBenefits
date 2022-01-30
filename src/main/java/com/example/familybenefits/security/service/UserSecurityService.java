package com.example.familybenefits.security.service;

import com.example.familybenefits.exception.InvalidEmailException;
import com.example.familybenefits.exception.PasswordNotEqualsException;
import com.example.familybenefits.exception.PasswordNotSafetyException;

/**
 * @author Oleg Maximov
 * @version 1.0
 * Интерфейс сервиса, отвечающего за данные пользователя
 */
public interface UserSecurityService {

  /**
   * Проверяет пароль на эквивалентность с повторно введенным и на соответствие политике безопасности паролей.
   * @param password проверяемый пароль
   * @param repeatPassword повторно введенный пароль
   * @param notEqMessage сообщение об ошибке о не эквивалентных паролях
   * @param notSfMessage сообщение об ошибке о не безопасном пароле
   * @throws PasswordNotEqualsException если указанные пароли не эквивалентны
   * @throws PasswordNotSafetyException если пароль не соотвествует политике безопасности
   */
  void checkPasswordElseThrow(String password, String repeatPassword, String notEqMessage, String notSfMessage) throws PasswordNotEqualsException, PasswordNotSafetyException;

  /**
   * Проверяет корректность email
   * @param email проверяемый email
   * @param message сообщение об ошибке
   * @throws InvalidEmailException если указанный "email" не является email
   */
  void checkEmailElseThrow(String email, String message) throws InvalidEmailException;
}
