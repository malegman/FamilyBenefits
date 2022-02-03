package com.example.familybenefits.resource;

/**
 * Класс ресурсов, предоставляющий числовые и строковые значения
 */
public class R {

  /**
   * Название типа объекта о пособии
   */
  public static final String NAME_OBJECT_BENEFIT = "Benefit";
  /**
   * Название типа объекта о городе
   */
  public static final String NAME_OBJECT_CITY = "City";
  /**
   * Название типа объекта о критерии
   */
  public static final String NAME_OBJECT_CRITERION = "Criterion";
  /**
   * Название типа объекта о типе критерия
   */
  public static final String NAME_OBJECT_CRITERION_TYPE = "Criterion type";
  /**
   * Название типа объекта об учреждении
   */
  public static final String NAME_OBJECT_INSTITUTION = "Institution";
  /**
   * Название типа объекта о пользователе
   */
  public static final String NAME_OBJECT_USER = "User";
  /**
   * Название типа объекта об администраторе
   */
  public static final String NAME_OBJECT_ADMIN = "Administrator";

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
