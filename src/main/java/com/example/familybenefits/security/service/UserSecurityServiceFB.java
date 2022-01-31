package com.example.familybenefits.security.service;

import com.example.familybenefits.exception.InvalidEmailException;
import com.example.familybenefits.exception.PasswordNotEqualsException;
import com.example.familybenefits.exception.PasswordNotSafetyException;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

/**
 * @author Oleg Maximov
 * @version 1.0
 * Реализация сервиса, отвечающего за данные пользователя
 */
@Service
public class UserSecurityServiceFB implements UserSecurityService {

  public static final Pattern PATTERN_EMAIL = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$");
  public static final Pattern PATTERN_PWD_LOWER = Pattern.compile("[a-zа-яё]");
  public static final Pattern PATTERN_PWD_UPPER = Pattern.compile("[A-ZА-ЯЁ]");
  public static final Pattern PATTERN_PWD_SIGNS = Pattern.compile("[~`!@#$%^&*()\\-_=+\\\\|;\"',<.>/?]");
  public static final Pattern PATTERN_PWD_NUMBER = Pattern.compile("[0-9]");

  /**
   * Проверяет пароль на эквивалентность с повторно введенным и на соответствие политике безопасности паролей.
   * @param password проверяемый пароль
   * @param repeatPassword повторно введенный пароль
   * @param notEqMessage сообщение об ошибке о не эквивалентных паролях
   * @param notSfMessage сообщение об ошибке о не безопасном пароле
   * @throws PasswordNotEqualsException если указанные пароли не эквивалентны
   * @throws PasswordNotSafetyException если пароль не соотвествует политике безопасности
   */
  @Override
  public void checkPasswordElseThrow(String password, String repeatPassword, String notEqMessage, String notSfMessage) throws PasswordNotEqualsException, PasswordNotSafetyException {

    if (!password.equals(repeatPassword)) {
      throw new PasswordNotEqualsException(notEqMessage);
    }
    
    if (!( PATTERN_PWD_LOWER.matcher(password).matches()
        && PATTERN_PWD_UPPER.matcher(password).matches()
        && PATTERN_PWD_SIGNS.matcher(password).matches()
        && PATTERN_PWD_NUMBER.matcher(password).matches())) {
      throw new PasswordNotSafetyException(notEqMessage);
    }
  }

  /**
   * Проверяет корректность email
   * @param email проверяемый email
   * @param message сообщение об ошибке
   * @throws InvalidEmailException если указанный "email" не является email
   */
  @Override
  public void checkEmailElseThrow(String email, String message) throws InvalidEmailException {

    if (!PATTERN_EMAIL.matcher(email).matches()) {
      throw new InvalidEmailException(message);
    }
  }
}