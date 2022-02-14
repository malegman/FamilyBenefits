package com.example.familybenefits.service.implementation;

import com.example.familybenefits.api_model.admin.AdminInfo;
import com.example.familybenefits.api_model.admin.AdminSave;
import com.example.familybenefits.convert.AdminDBConverter;
import com.example.familybenefits.dto.entity.UserEntity;
import com.example.familybenefits.dto.repository.UserRepository;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.InvalidEmailException;
import com.example.familybenefits.exception.NotFoundException;
import com.example.familybenefits.exception.UserRoleException;
import com.example.familybenefits.resource.R;
import com.example.familybenefits.security.service.s_interface.DBIntegrityService;
import com.example.familybenefits.security.service.s_interface.UserSecurityService;
import com.example.familybenefits.service.s_interface.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
   * Конструктор для инициализации интерфейсов репозиториев и сервисов
   * @param userRepository репозиторий, работающий с моделью таблицы "user"
   * @param dbIntegrityService интерфейс сервиса, отвечающего за целостность базы данных
   * @param userSecurityService интерфейс сервиса, отвечающего за данные пользователя
   */
  @Autowired
  public AdminServiceFB(UserRepository userRepository,
                        DBIntegrityService dbIntegrityService,
                        UserSecurityService userSecurityService) {
    this.userRepository = userRepository;
    this.dbIntegrityService = dbIntegrityService;
    this.userSecurityService = userSecurityService;
  }

  /**
   * Создает администратора по запросу на сохранение
   * @param adminSave объект запроса на сохранение администратора
   * @throws AlreadyExistsException если администратор или пользователь с указанным email уже существует
   * @throws InvalidEmailException если указанный "email" не является email
   */
  @Override
  public void create(AdminSave adminSave) throws AlreadyExistsException, InvalidEmailException {

    // Проверка строки email на соответствие формату email
    userSecurityService.checkEmailElseThrowInvalidEmail(
        adminSave.getEmail());

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    UserEntity userEntityFromSave = AdminDBConverter
        .fromSave(adminSave, dbIntegrityService::preparePostgreSQLString);

    // Проверка на существование пользователя или администратора по email
    dbIntegrityService.checkAbsenceByUniqStr(
        userRepository::existsByEmail, userEntityFromSave.getEmail());

    // Передача роли "ROLE_SUPER_ADMIN" новому администратору, если он первый администратор
    UserEntity userEntitySuperAdmin = userRepository.getSuperAdmin();
    if (userEntitySuperAdmin.getEmail().equals(R.DEFAULT_SUPER_ADMIN_EMAIL)) {
      userEntityFromSave.addRole(R.ROLE_SUPER_ADMIN);
      userEntityFromSave.setId(userEntitySuperAdmin.getId());
    }

    userEntityFromSave.addRole(R.ROLE_ADMIN);

    userRepository.saveAndFlush(userEntityFromSave);
  }

  /**
   * Возвращает администратора об учреждении по его ID
   * @param idAdmin ID администратора
   * @return информация об администраторе
   * @throws NotFoundException если администратор с данным ID не найдено
   */
  @Override
  public AdminInfo read(String idAdmin) throws NotFoundException {

    String prepareIdAdmin = dbIntegrityService.preparePostgreSQLString(idAdmin);

    // Получение пользователя по его ID, если пользователь существует
    UserEntity userEntityFromRequest = userRepository.findById(prepareIdAdmin)
        .orElseThrow(() -> new NotFoundException(String.format(
            "Administrator with ID \"%s\" not found", idAdmin)));

    // Проверка наличия роли "ROLE_ADMIN" у пользователя
    userSecurityService.checkHasRoleElseThrowNotFound(
        userEntityFromRequest, R.ROLE_ADMIN, R.CLIENT_ADMIN);

    return AdminDBConverter.toInfo(userEntityFromRequest);
  }

  /**
   * Обновляет администратора по запросу на сохранение
   * @param idAdmin ID администратора
   * @param adminSave объект запроса на сохранение администратора
   * @throws NotFoundException если администратор с указанными данными не найден
   * @throws InvalidEmailException если указанный "email" не является email
   * @throws AlreadyExistsException если администратор или пользователь с отличным ID и данным email уже существует
   */
  @Override
  public void update(String idAdmin, AdminSave adminSave) throws NotFoundException, InvalidEmailException, AlreadyExistsException {

    // Проверка строки email на соответствие формату email
    userSecurityService.checkEmailElseThrowInvalidEmail(
        adminSave.getEmail());

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    UserEntity userEntityFromSave = AdminDBConverter
        .fromSave(adminSave, dbIntegrityService::preparePostgreSQLString);

    String prepareIdAdmin = dbIntegrityService.preparePostgreSQLString(idAdmin);

    // Проверка отсутствия пользователя с отличным от данного ID и данным email
    dbIntegrityService.checkAbsenceAnotherByUniqStr(
        userRepository::existsByIdIsNotAndName, prepareIdAdmin, userEntityFromSave.getEmail());

    // Получение пользователя по его ID, если пользователь существует
    UserEntity userEntityFromDB = userRepository.findById(prepareIdAdmin)
        .orElseThrow(() -> new NotFoundException(String.format(
            "Administrator with ID \"%s\" not found", userEntityFromSave.getId())));

    // Проверка наличия роли "ROLE_ADMIN" у пользователя
    userSecurityService.checkHasRoleElseThrowNotFound(
        userEntityFromDB, R.ROLE_ADMIN, R.CLIENT_ADMIN);

    userEntityFromDB.setEmail(userEntityFromSave.getEmail());
    userEntityFromDB.setName(userEntityFromSave.getName());

    userRepository.saveAndFlush(userEntityFromDB);
  }

  /**
   * Удаляет администратора по его ID или удаляет роль "ROLE_ADMIN" у пользователя
   * @param idAdmin ID администратора
   * @throws NotFoundException если администратор с указанным ID не найден
   * @throws UserRoleException если администратор имеет роль "ROLE_SUPER_ADMIN"
   */
  @Override
  public void delete(String idAdmin) throws NotFoundException, UserRoleException {

    String prepareIdAdmin = dbIntegrityService.preparePostgreSQLString(idAdmin);

    // Получение пользователя по его ID, если пользователь существует
    UserEntity userEntityFromRequest = userRepository.findById(prepareIdAdmin)
        .orElseThrow(() -> new NotFoundException(String.format(
            "Administrator with ID \"%s\" not found", idAdmin)));

    // Проверка наличия роли "ROLE_ADMIN" у пользователя
    userSecurityService.checkHasRoleElseThrowNotFound(
        userEntityFromRequest, R.ROLE_ADMIN, R.CLIENT_ADMIN);

    // Проверка отсутствия роли "ROLE_SUPER_ADMIN" у пользователя
    userSecurityService.checkNotHasRoleElseThrowUserRole(
        userEntityFromRequest, R.ROLE_SUPER_ADMIN, R.CLIENT_ADMIN);

    // Если есть роль "ROLE_USER", удаление роли "ROLE_ADMIN", иначе удаление пользователя
    if (userEntityFromRequest.hasRole(R.ROLE_USER)) {
      userEntityFromRequest.removeRole(R.ROLE_ADMIN);
      userRepository.saveAndFlush(userEntityFromRequest);
    } else {
      userRepository.deleteById(prepareIdAdmin);
    }
  }

  /**
   * Добавляет роль "ROLE_ADMIN" пользователю
   * @param idUser ID пользователя
   * @throws NotFoundException если пользователь с данным ID не найден
   * @throws UserRoleException если пользователь имеет роль "ROLE_ADMIN" или не имеет роль "ROLE_USER"
   */
  @Override
  public void fromUser(String idUser) throws NotFoundException, UserRoleException {

    String prepareIdUser = dbIntegrityService.preparePostgreSQLString(idUser);

    // Получение пользователя по его ID, если пользователь существует
    UserEntity userEntityFromRequest = userRepository.findById(prepareIdUser)
        .orElseThrow(() -> new NotFoundException(String.format(
            "User with ID \"%s\" not found", idUser)));

    // Проверка наличия роли "ROLE_USER" у пользователя
    userSecurityService.checkHasRoleElseThrowNotFound(
        userEntityFromRequest, R.ROLE_USER, R.CLIENT_USER);

    // Проверка отсутствия роли "ROLE_ADMIN" у пользователя
    userSecurityService.checkNotHasRoleElseThrowUserRole(
        userEntityFromRequest, R.ROLE_ADMIN, R.CLIENT_USER);

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
  public void toUser(String idAdmin) throws NotFoundException, UserRoleException {

    String prepareIdAdmin = dbIntegrityService.preparePostgreSQLString(idAdmin);

    // Получение пользователя по его ID, если пользователь существует
    UserEntity userEntityFromRequest = userRepository.findById(prepareIdAdmin)
        .orElseThrow(() -> new NotFoundException(String.format(
            "Administrator with ID \"%s\" not found", idAdmin)));

    // Проверка наличия роли "ROLE_ADMIN" у пользователя
    userSecurityService.checkHasRoleElseThrowNotFound(
        userEntityFromRequest, R.ROLE_ADMIN, R.CLIENT_ADMIN);

    // Проверка отсутствия роли "ROLE_USER" у пользователя
    userSecurityService.checkNotHasRoleElseThrowUserRole(
        userEntityFromRequest, R.ROLE_USER, R.CLIENT_ADMIN);

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
  public void toSuper(String idAdmin) throws NotFoundException, UserRoleException {

    String prepareIdAdmin = dbIntegrityService.preparePostgreSQLString(idAdmin);

    // Получение пользователя по его ID, если пользователь существует
    UserEntity userEntityFromRequest = userRepository.findById(prepareIdAdmin)
        .orElseThrow(() -> new NotFoundException(String.format(
            "Administrator with ID \"%s\" not found", idAdmin)));

    // Проверка наличия роли "ROLE_ADMIN" у пользователя
    userSecurityService.checkHasRoleElseThrowNotFound(
        userEntityFromRequest, R.ROLE_ADMIN, R.CLIENT_ADMIN);

    // Проверка отсутствия роли "ROLE_SUPER_ADMIN" у пользователя
    userSecurityService.checkNotHasRoleElseThrowUserRole(
        userEntityFromRequest, R.ROLE_SUPER_ADMIN, R.CLIENT_ADMIN);

    // Передача роли "ROLE_SUPER_ADMIN"
    UserEntity userEntitySuperAdmin = userRepository.getSuperAdmin();
    userEntitySuperAdmin.removeRole(R.ROLE_SUPER_ADMIN);
    userEntityFromRequest.addRole(R.ROLE_SUPER_ADMIN);

    userRepository.saveAndFlush(userEntitySuperAdmin);
    userRepository.saveAndFlush(userEntityFromRequest);
  }
}
