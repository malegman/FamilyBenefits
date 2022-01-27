package com.example.familybenefits.service;

import com.example.familybenefits.api_model.admin.AdminAdd;
import com.example.familybenefits.api_model.admin.AdminInfo;
import com.example.familybenefits.api_model.admin.AdminUpdate;
import com.example.familybenefits.convert.AdminConverter;
import com.example.familybenefits.dao.entity.RoleEntity;
import com.example.familybenefits.dao.entity.UserEntity;
import com.example.familybenefits.dao.repository.UserRepository;
import com.example.familybenefits.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Optional;

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
   * Конструктор для инициализации интерфейсов репозиториев
   * @param userRepository репозиторий, работающий с моделью таблицы "user"
   * @param dbIntegrityService интерфейс сервиса, отвечающего за целостность базы данных
   * @param userSecurityService интерфейс сервиса, отвечающего за данные пользователя
   */
  @Autowired
  public AdminServiceFB(UserRepository userRepository, DBIntegrityService dbIntegrityService,
                        UserSecurityService userSecurityService) {
    this.userRepository = userRepository;
    this.dbIntegrityService = dbIntegrityService;
    this.userSecurityService = userSecurityService;
  }

  /**
   * Добавляет администратора по запросу на добавление
   * @param adminAdd adminAdd объект запроса на добавление администратора
   * @throws AlreadyExistsException если администратор или пользователь с указанным email уже существует
   * @throws PasswordNotSafetyException если пароль не соотвествует политике безопасности
   * @throws PasswordNotEqualsException если указанные пароли не эквивалентны
   * @throws InvalidEmailException если указанный "email" не является email
   */
  @Override
  public void add(AdminAdd adminAdd) throws AlreadyExistsException, PasswordNotSafetyException, PasswordNotEqualsException, InvalidEmailException {

    UserEntity userEntityFromDB;
    UserEntity userEntitySuperAdmin;

    userSecurityService.checkPasswordElseThrow(
        adminAdd.getPassword(), adminAdd.getRepeatPassword(),
        "Input passwords are not equals",
        "Input password is not safety");

    userSecurityService.checkEmailElseThrow(
        adminAdd.getEmail(),
        "Input value is not an email");

    UserEntity userEntityFromAdd = (UserEntity) AdminConverter
        .fromAdd(adminAdd)
        .prepareForDB(dbIntegrityService::preparePostgreSQLString);

    Optional<UserEntity> optUserEntityFromDB = userRepository.findByEmail(userEntityFromAdd.getEmail());

    if (optUserEntityFromDB.isPresent()) {
      userEntityFromDB = optUserEntityFromDB.get();
      for (RoleEntity roleEntity : userEntityFromDB.getRoleEntitySet()) {
        if (roleEntity.getName().equals("ROLE_ADMIN")) {
          throw new AlreadyExistsException(String.format(
              "The administrator with email %s already exists", userEntityFromAdd.getEmail()));
        }
        if (roleEntity.getName().equals("ROLE_USER")) {
          throw new AlreadyExistsException(String.format(
              "The user with email %s already exists", userEntityFromAdd.getEmail()));
        }
      }
    }

    userEntitySuperAdmin = userRepository.getSuperAdmin();

    if (userEntitySuperAdmin.getEmail().equals("email")) {
      userEntityFromAdd.setRoleEntitySet(userEntitySuperAdmin.getRoleEntitySet());
      userRepository.delete(userEntitySuperAdmin);
    }

    userEntityFromAdd.addRole("ROLE_ADMIN");
    userEntityFromAdd.setVerifiedEmail(false);

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

    UserEntity userEntityFromDB;
    boolean isAdmin = false;

    userSecurityService.checkEmailElseThrow(
        adminUpdate.getEmail(),
        "Input value is not an email");

    UserEntity userEntityFromUpdate = (UserEntity) AdminConverter
        .fromUpdate(adminUpdate)
        .prepareForDB(dbIntegrityService::preparePostgreSQLString);

    Optional<UserEntity> optUserEntityFromDB = userRepository.findById(userEntityFromUpdate.getId());

    if (optUserEntityFromDB.isEmpty()) {
      throw new NotFoundException(String.format(
          "Administrator with ID %s not found", userEntityFromUpdate.getId()));
    }

    userEntityFromDB = optUserEntityFromDB.get();

    for (RoleEntity roleEntity : userEntityFromDB.getRoleEntitySet()) {
      if (roleEntity.getName().equals("ROLE_ADMIN")) {
        isAdmin = true;
        break;
      }
    }

    if (!isAdmin) {
      throw new NotFoundException(String.format(
          "Administrator with ID %s not found", userEntityFromUpdate.getId()));
    }

    if (!userEntityFromUpdate.getEmail().equals(userEntityFromDB.getEmail())) {
      userEntityFromDB.setVerifiedEmail(false);
      userEntityFromDB.setEmail(userEntityFromUpdate.getEmail());
    }
    userEntityFromDB.setDateBirth(userEntityFromUpdate.getDateBirth());
    userEntityFromDB.setName(userEntityFromUpdate.getName());

    userRepository.saveAndFlush(userEntityFromDB);
  }

  /**
   * Удаляет администратора по его ID или удаляет роль администратора у пользователя
   * @param idAdmin ID администратора
   * @throws NotFoundException если администратор с указанным ID не найден
   */
  @Override
  public void delete(BigInteger idAdmin) throws NotFoundException {

    UserEntity userEntityFromDB;
    boolean isUser = false;

    Optional<UserEntity> optUserEntityFromDB = userRepository.findById(idAdmin);

    if (optUserEntityFromDB.isEmpty()) {
      throw new NotFoundException(String.format(
          "Administrator with ID %s not found", idAdmin));
    }

    userEntityFromDB = optUserEntityFromDB.get();

    for (RoleEntity roleEntity : userEntityFromDB.getRoleEntitySet()) {
      if (roleEntity.getName().equals("ROLE_USER")) {
        isUser = true;
        break;
      }
    }

    if (isUser) {
      userEntityFromDB.removeRole("ROLE_ADMIN");
      userRepository.saveAndFlush(userEntityFromDB);
    } else {
      userRepository.deleteById(idAdmin);
    }
  }

  /**
   * Возвращает администратора об учреждении по его ID
   * @param idAdmin ID администратора
   * @return информация об администраторе
   * @throws NotFoundException если администратор с указанным ID не найдено
   */
  @Override
  public AdminInfo read(BigInteger idAdmin) throws NotFoundException {

    Optional<UserEntity> optUserEntityFromDB = userRepository.findById(idAdmin);

    if (optUserEntityFromDB.isEmpty()) {
      throw new NotFoundException(String.format(
          "Administrator with ID %s not found", idAdmin));
    }

    return AdminConverter.toInfo(optUserEntityFromDB.get());
  }

  /**
   * Добавляет роль администратора пользователю
   * @param idUser ID пользователя
   * @throws NotFoundException если пользователь с данным ID не найден
   * @throws AlreadyExistsException если пользователь имеет роль администратора
   */
  @Override
  public void fromUser(BigInteger idUser) throws NotFoundException, AlreadyExistsException {

    UserEntity userEntityFromDB;
    boolean isAdmin = false;

    Optional<UserEntity> optUserEntityFromDB = userRepository.findById(idUser);

    if (optUserEntityFromDB.isEmpty()) {
      throw new NotFoundException(String.format(
          "User with ID %s not found", idUser));
    }

    userEntityFromDB = optUserEntityFromDB.get();

    for (RoleEntity roleEntity : userEntityFromDB.getRoleEntitySet()) {
      if (roleEntity.getName().equals("ROLE_ADMIN")) {
        isAdmin = true;
        break;
      }
    }

    if (!isAdmin) {
      throw new AlreadyExistsException(String.format(
          "User with ID %s already has role \"ROLE_ADMIN\"", idUser));
    }

    userEntityFromDB.addRole("ROLE_ADMIN");

    userRepository.saveAndFlush(userEntityFromDB);
  }

  /**
   * Добавляет роль пользователя администратору
   * @param idAdmin ID администратора
   * @throws NotFoundException если администратор с данным ID не найден
   * @throws AlreadyExistsException если администратор имеет роль пользователя
   */
  @Override
  public void toUser(BigInteger idAdmin) throws NotFoundException, AlreadyExistsException {

    UserEntity userEntityFromDB;
    boolean isUser = false;

    Optional<UserEntity> optUserEntityFromDB = userRepository.findById(idAdmin);

    if (optUserEntityFromDB.isEmpty()) {
      throw new NotFoundException(String.format(
          "Administrator with ID %s not found", idAdmin));
    }

    userEntityFromDB = optUserEntityFromDB.get();

    for (RoleEntity roleEntity : userEntityFromDB.getRoleEntitySet()) {
      if (roleEntity.getName().equals("ROLE_USER")) {
        isUser = true;
        break;
      }
    }

    if (isUser) {
      throw new AlreadyExistsException(String.format(
          "Administrator with ID %s already has role \"ROLE_USER\"", idAdmin));
    }

    userEntityFromDB.addRole("ROLE_USER");

    userRepository.saveAndFlush(userEntityFromDB);
  }
}
