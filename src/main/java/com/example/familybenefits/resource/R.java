package com.example.familybenefits.resource;

/**
 * Класс ресурсов, предоставляющий числовые и строковые значения
 */
public class R {

  /**
   * Название клиента с ролью пользователя
   */
  public static final String CLIENT_USER = "User";
  /**
   * Название клиента с ролью администратора
   */
  public static final String CLIENT_ADMIN = "Administrator";

  /**
   * Роль пользователя
   */
  public static final String ROLE_USER = "ROLE_USER";
  /**
   * Роль администратора
   */
  public static final String ROLE_ADMIN = "ROLE_ADMIN";
  /**
   * Роль супер-администратора
   */
  public static final String ROLE_SUPER_ADMIN = "ROLE_SUPER_ADMIN";

  /**
   * Почта начального супер-администратора
   */
  public static final String DEFAULT_SUPER_ADMIN_EMAIL = "email";
  /**
   * Пароль начального супер-администратора
   */
  public static final String DEFAULT_SUPER_ADMIN_PASSWORD = "fbsupadm";

  /**
   * Закрытый ключ, используемый для подписывания jwt
   */
  public static final String JWT_SECRET = "familyben";
  /**
   * Время жизни jwt в минутах
   */
  public static final int JWT_EXPIRATION_MIN = 15;
}
