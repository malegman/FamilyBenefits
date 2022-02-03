package com.example.familybenefits.service.implementation;

import com.example.familybenefits.resource.R;
import com.example.familybenefits.api_model.admin.AdminAdd;
import com.example.familybenefits.api_model.admin.AdminInfo;
import com.example.familybenefits.api_model.admin.AdminUpdate;
import com.example.familybenefits.convert.AdminConverter;
import com.example.familybenefits.dao.entity.UserEntity;
import com.example.familybenefits.dao.repository.UserRepository;
import com.example.familybenefits.exception.*;
import com.example.familybenefits.security.service.s_interface.DBIntegrityService;
import com.example.familybenefits.security.service.s_interface.UserSecurityService;
import com.example.familybenefits.service.s_interface.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

/**
 * Реализация сервиса, управляющего объектом "администратор"
 */
@Service
public class AdminServiceFB implements AdminService {

  /**
   * Репозиторий, работающий с моделью таблицы "user"
   */
  private final UserRepository userRepository;

  /**
   * Интерфейс сервиса, отвечающего за целостность базы данных
   */
  private final DBIntegrityService dbIntegrityService;
  /**
   * Интерфейс сервиса, отвечающего за данные пользователя
   */
  private final UserSecurityService userSecurityService;
  /**
   * Интерфейс сервиса для шифрования паролей
   */
  private final PasswordEncoder passwordEncoder;

  /**
   * Конструктор для инициализации интерфейсов репозиториев и сервисов
   * @param userRepository репозиторий, работающий с моделью таблицы "user"
   * @param dbIntegrityService интерфейс сервиса, отвечающего за целостность базы данных
   * @param userSecurityService интерфейс сервиса, отвечающего за данные пользователя
   * @param passwordEncoder интерфейс сервиса для шифрования паролей
   */
  @Autowired
  public AdminServiceFB(UserRepository userRepository,
                        DBIntegrityService dbIntegrityService,
                        UserSecurityService userSecurityService,
                        PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.dbIntegrityService = dbIntegrityService;
    this.userSecurityService = userSecurityService;
    this.passwordEncoder = passwordEncoder;
  }

  /**
   * Добавляет администратора по запросу на добавление
   * @param adminAdd adminAdd объект запроса на добавление администратора
   * @throws AlreadyExistsException если администратор или пользователь с указанным email уже существует
   * @throws PasswordNotSafetyException если пароль не соответствует политике безопасности
   * @throws PasswordNotEqualsException если указанные пароли не эквивалентны
   * @throws InvalidEmailException если указанный "email" не является email
   */
  @Override
  public void add(AdminAdd adminAdd) throws AlreadyExistsException, PasswordNotSafetyException, PasswordNotEqualsException, InvalidEmailException {

    // Проверка паролей на эквивалентность и безопасность
    userSecurityService.checkPasswordElseThrow(
        adminAdd.getPassword(), adminAdd.getRepeatPassword());

    // Проверка строки email на соответствие формату email
    userSecurityService.checkEmailElseThrowInvalidEmail(
        adminAdd.getEmail());

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    UserEntity userEntityFromAdd = (UserEntity) AdminConverter
        .fromAdd(adminAdd)
        .prepareForDB(dbIntegrityService::preparePostgreSQLString);

    // Проверка на существование пользователя или администратора по email
    dbIntegrityService.checkAbsenceByUniqStrElseThrowAlreadyExists(
        userRepository::existsByEmail, userEntityFromAdd.getEmail(), R.NAME_OBJECT_USER);

    // Передача роли "ROLE_SUPER_ADMIN" новому администратору, если он первый администратор
    UserEntity userEntitySuperAdmin = userRepository.getSuperAdmin();
    if (userEntitySuperAdmin.getEmail().equals(R.DEFAULT_SUPER_ADMIN_EMAIL)) {
      userEntityFromAdd.addRole(R.ROLE_SUPER_ADMIN);
      userEntityFromAdd.setId(userEntitySuperAdmin.getId());
    }

    userEntityFromAdd.addRole(R.ROLE_ADMIN);
    userEntityFromAdd.setVerifiedEmail(false);
    userEntityFromAdd.encryptPassword(passwordEncoder::encode);

    userRepository.saveAndFlush(userEntityFromAdd);
  }

  /**
   * Обновляет администратора по запросу на обновление
   * @param adminUpdate объект запроса на обновление администратора
   * @throws NotFoundException если администратор с указанными данными не найден
   * @throws InvalidEmailException если указанный "email" не является email
   */
  @Override
  public void update(AdminUpdate adminUpdate) throws NotFoundException, InvalidEmailException {

    // Проверка строки email на соответствие формату email
    userSecurityService.checkEmailElseThrowInvalidEmail(
        adminUpdate.getEmail());

    // Получение пользователя по его ID, если пользователь существует
    UserEntity userEntityFromDB = userRepository.findById(adminUpdate.getId())
        .orElseThrow(() -> new NotFoundException(String.format(
            "Administrator with ID \"%s\" not found", adminUpdate.getId())));

    // Проверка наличия роли "ROLE_ADMIN" у пользователя
    userSecurityService.checkHasRoleElseThrowNotFound(
        userEntityFromDB, R.ROLE_ADMIN, R.NAME_OBJECT_ADMIN);

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    UserEntity userEntityFromUpdate = (UserEntity) AdminConverter
        .fromUpdate(adminUpdate)
        .prepareForDB(dbIntegrityService::preparePostgreSQLString);

    // Изменение email и сброс его подтверждения, если новое email
    if (!userEntityFromUpdate.getEmail().equals(userEntityFromDB.getEmail())) {
      userEntityFromDB.setVerifiedEmail(false);
      userEntityFromDB.setEmail(userEntityFromUpdate.getEmail());
    }

    userEntityFromDB.setName(userEntityFromUpdate.getName());

    userRepository.saveAndFlush(userEntityFromDB);
  }

  /**
   * Удаляет администратора по его ID или удаляет роль "ROLE_ADMIN" у пользователя
   * @param idAdmin ID администратора
   * @throws NotFoundException если администратор с указанным ID не найден
   * @throws UserRoleException если администратор имеет роль "ROLE_SUPER_ADMIN"
   */
  @Override
  public void delete(BigInteger idAdmin) throws NotFoundException, UserRoleException {

    // Получение пользователя по его ID, если пользователь существует
    UserEntity userEntityFromRequest = userRepository.findById(idAdmin)
        .orElseThrow(() -> new NotFoundException(String.format(
            "Administrator with ID \"%s\" not found", idAdmin)));

    // Проверка наличия роли "ROLE_ADMIN" у пользователя
    userSecurityService.checkHasRoleElseThrowNotFound(
        userEntityFromRequest, R.ROLE_ADMIN, R.NAME_OBJECT_ADMIN);

    // Проверка отсутствия роли "ROLE_SUPER_ADMIN" у пользователя
    userSecurityService.checkNotHasRoleElseThrowUserRole(
        userEntityFromRequest, R.ROLE_SUPER_ADMIN, R.NAME_OBJECT_ADMIN);

    // Если есть роль "ROLE_USER", удаление роли "ROLE_ADMIN", иначе удаление пользователя
    if (userEntityFromRequest.hasRole(R.ROLE_USER)) {
      userEntityFromRequest.removeRole(R.ROLE_ADMIN);
      userRepository.saveAndFlush(userEntityFromRequest);
    } else {
      userRepository.deleteById(idAdmin);
    }
  }

  /**
   * Возвращает администратора об учреждении по его ID
   * @param idAdmin ID администратора
   * @return информация об администраторе
   * @throws NotFoundException если администратор с данным ID не найдено
   */
  @Override
  public AdminInfo read(BigInteger idAdmin) throws NotFoundException {

    // Получение пользователя по его ID, если пользователь существует
    UserEntity userEntityFromRequest = userRepository.findById(idAdmin)
        .orElseThrow(() -> new NotFoundException(String.format(
            "Administrator with ID \"%s\" not found", idAdmin)));

    // Проверка наличия роли "ROLE_ADMIN" у пользователя
    userSecurityService.checkHasRoleElseThrowNotFound(
        userEntityFromRequest, R.ROLE_ADMIN, R.NAME_OBJECT_ADMIN);

    return AdminConverter.toInfo(userEntityFromRequest);
  }

  /**
   * Добавляет роль "ROLE_ADMIN" пользователю
   * @param idUser ID пользователя
   * @throws NotFoundException если пользователь с данным ID не найден
   * @throws UserRoleException если пользователь имеет роль "ROLE_ADMIN" или не имеет роль "ROLE_USER"
   */
  @Override
  public void fromUser(BigInteger idUser) throws NotFoundException, UserRoleException {

    // Получение пользователя по его ID, если пользователь существует
    UserEntity userEntityFromRequest = userRepository.findById(idUser)
        .orElseThrow(() -> new NotFoundException(String.format(
            "User with ID \"%s\" not found", idUser)));

    // Проверка наличия роли "ROLE_USER" у пользователя
    userSecurityService.checkHasRoleElseThrowNotFound(
        userEntityFromRequest, R.ROLE_USER, R.NAME_OBJECT_USER);

    // Проверка отсутствия роли "ROLE_ADMIN" у пользователя
    userSecurityService.checkNotHasRoleElseThrowUserRole(
        userEntityFromRequest, R.ROLE_ADMIN, R.NAME_OBJECT_USER);

    userEntityFromRequest.addRole(R.ROLE_ADMIN);

    userRepository.saveAndFlush(userEntityFromRequest);
  }

  /**
   * Добавляет роль "ROLE_USER" администратору
   * @param idAdmin ID администратора
   * @throws NotFoundException если администратор с данным ID не найден
   * @throws UserRoleException если пользователь имеет роль "ROLE_USER" или не имеет роль "ROLE_ADMIN"
   */
  @Override
  public void toUser(BigInteger idAdmin) throws NotFoundException, UserRoleException {

    // Получение пользователя по его ID, если пользователь существует
    UserEntity userEntityFromRequest = userRepository.findById(idAdmin)
        .orElseThrow(() -> new NotFoundException(String.format(
            "Administrator with ID \"%s\" not found", idAdmin)));

    // Проверка наличия роли "ROLE_ADMIN" у пользователя
    userSecurityService.checkHasRoleElseThrowNotFound(
        userEntityFromRequest, R.ROLE_ADMIN, R.NAME_OBJECT_ADMIN);

    // Проверка отсутствия роли "ROLE_USER" у пользователя
    userSecurityService.checkNotHasRoleElseThrowUserRole(
        userEntityFromRequest, R.ROLE_USER, R.NAME_OBJECT_ADMIN);

    userEntityFromRequest.addRole(R.ROLE_USER);

    userRepository.saveAndFlush(userEntityFromRequest);
  }

  /**
   * Передает роль "ROLE_SUPER_ADMIN" указанному администратору, удаляя данную роль у текущего администратора
   * @param idAdmin ID администратора
   * @throws NotFoundException если администратор с данным ID не найден
   * @throws UserRoleException если администратор имеет роль "ROLE_SUPER_ADMIN"
   */
  @Override
  public void toSuper(BigInteger idAdmin) throws NotFoundException, UserRoleException {

    // Получение пользователя по его ID, если пользователь существует
    UserEntity userEntityFromRequest = userRepository.findById(idAdmin)
        .orElseThrow(() -> new NotFoundException(String.format(
            "Administrator with ID \"%s\" not found", idAdmin)));

    // Проверка наличия роли "ROLE_ADMIN" у пользователя
    userSecurityService.checkHasRoleElseThrowNotFound(
        userEntityFromRequest, R.ROLE_ADMIN, R.NAME_OBJECT_ADMIN);

    // Проверка отсутствия роли "ROLE_SUPER_ADMIN" у пользователя
    userSecurityService.checkNotHasRoleElseThrowUserRole(
        userEntityFromRequest, R.ROLE_SUPER_ADMIN, R.NAME_OBJECT_ADMIN);

    // Передача роли "ROLE_SUPER_ADMIN"
    UserEntity userEntitySuperAdmin = userRepository.getSuperAdmin();
    userEntitySuperAdmin.removeRole(R.ROLE_SUPER_ADMIN);
    userEntityFromRequest.addRole(R.ROLE_SUPER_ADMIN);

    userRepository.saveAndFlush(userEntitySuperAdmin);
    userRepository.saveAndFlush(userEntityFromRequest);
  }
}
