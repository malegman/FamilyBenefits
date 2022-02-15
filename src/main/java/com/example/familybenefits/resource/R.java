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
  public static final String DEFAULT_SUPER_ADMIN_EMAIL = "smegovic@gmail.com";

  /**
   * Закрытый ключ, используемый для подписывания jwt
   */
  public static final String JWT_SECRET = "fj8fg7fhsm,v01fhb";
  /**
   * Время жизни jwt (sec) (20 min)
   */
  public static final int JWT_EXPIRATION_SEC = 60 * 20;
  /**
   * Время жизни login code (sec) (1 hour)
   */
  public static final int LOGIN_EXPIRATION_SEC = 60 * 60 * 1;

  /**
   * Название заголовка "Authorization", для хранения токена доступа jwt
   */
  public static final String AUTHORIZATION_HEADER = "Authorization";
  /**
   * Префикс токена доступа в заголовке "Authorization"
   */
  public static final String ACCESS_TOKEN_PREFIX = "Bearer ";
  /**
   * Шаблон содержимого заголовка "Authorization", с параметром jwt
   */
  public static final String AUTHORIZATION_VALUE_PATTERN = "Bearer %s";
}
