package com.example.familybenefits.service.implementation;

import com.example.familybenefits.api_model.user.UserInfo;
import com.example.familybenefits.api_model.user.UserInitData;
import com.example.familybenefits.api_model.user.UserSave;
import com.example.familybenefits.convert.CityDBConverter;
import com.example.familybenefits.convert.CriterionDBConverter;
import com.example.familybenefits.convert.UserDBConverter;
import com.example.familybenefits.dto.entity.ChildEntity;
import com.example.familybenefits.dto.entity.CityEntity;
import com.example.familybenefits.dto.entity.CriterionEntity;
import com.example.familybenefits.dto.entity.UserEntity;
import com.example.familybenefits.dto.repository.ChildRepository;
import com.example.familybenefits.dto.repository.CityRepository;
import com.example.familybenefits.dto.repository.CriterionRepository;
import com.example.familybenefits.dto.repository.UserRepository;
import com.example.familybenefits.exception.*;
import com.example.familybenefits.resource.R;
import com.example.familybenefits.security.service.s_interface.DBIntegrityService;
import com.example.familybenefits.security.service.s_interface.UserSecurityService;
import com.example.familybenefits.service.s_interface.DateTimeService;
import com.example.familybenefits.service.s_interface.EntityDBService;
import com.example.familybenefits.service.s_interface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Реализация сервиса, управляющего объектом "пользователь"
 */
@Service
public class UserServiceFB implements UserService {

  /**
   * Репозиторий, работающий с моделью таблицы "user"
   */
  private final UserRepository userRepository;

  /**
   * Репозиторий, работающий с моделью таблицы "child"
   */
  private final ChildRepository childRepository;

  /**
   * Интерфейс сервиса модели таблицы "city", целостность которой зависит от связанных таблиц
   */
  private final EntityDBService<CityEntity, CityRepository> cityDBService;
  /**
   * Интерфейс сервиса модели таблицы "criterion", целостность которой зависит от связанных таблиц
   */
  private final EntityDBService<CriterionEntity, CriterionRepository> criterionDBService;

  /**
   * Интерфейс сервиса, который предоставляет методы для работы с датой и временем
   */
  private final DateTimeService dateTimeService;

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
   * @param childRepository репозиторий, работающий с моделью таблицы "child"
   * @param cityDBService интерфейс сервиса модели таблицы "city", целостность которой зависит от связанных таблиц
   * @param criterionDBService интерфейс сервиса модели таблицы "criterion", целостность которой зависит от связанных таблиц
   * @param dateTimeService интерфейс сервиса, который предоставляет методы для работы с датой и временем
   * @param dbIntegrityService интерфейс сервиса, отвечающего за целостность базы данных
   * @param userSecurityService интерфейс сервиса, отвечающего за данные пользователя
   */
  @Autowired
  public UserServiceFB(UserRepository userRepository,
                       ChildRepository childRepository,
                       EntityDBService<CityEntity, CityRepository> cityDBService,
                       EntityDBService<CriterionEntity, CriterionRepository> criterionDBService,
                       DateTimeService dateTimeService,
                       DBIntegrityService dbIntegrityService,
                       UserSecurityService userSecurityService) {
    this.userRepository = userRepository;
    this.childRepository = childRepository;
    this.cityDBService = cityDBService;
    this.criterionDBService = criterionDBService;
    this.dateTimeService = dateTimeService;
    this.dbIntegrityService = dbIntegrityService;
    this.userSecurityService = userSecurityService;
  }

  /**
   * Создает пользователя по запросу на сохранение. Регистрация гостя
   * @param userSave объект запроса на сохранение пользователя
   * @throws NotFoundException если город или критерии с указанными данными не найдены
   * @throws AlreadyExistsException если администратор или пользователь с указанным email уже существует
   * @throws InvalidEmailException если указанный "email" не является email
   * @throws DateFormatException если даты рождения пользователя или детей не соответствуют формату "dd.mm.yyyy"
   * @throws DateTimeException если даты рождения пользователя или детей позже текущей даты
   */
  @Override
  public void create(UserSave userSave) throws
      NotFoundException,
      AlreadyExistsException,
      InvalidEmailException,
      DateFormatException,
      DateTimeException {

    // Проверка строки email на соответствие формату email
    userSecurityService.checkEmailElseThrowInvalidEmail(
        userSave.getEmail());

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    UserEntity userEntityFromSave = UserDBConverter
        .fromSave(userSave, dbIntegrityService::preparePostgreSQLString);

    // Проверка существования города и критерий по их ID
    dbIntegrityService.checkExistenceById(
        cityDBService.getRepository()::existsById, userEntityFromSave.getCityEntity());
    dbIntegrityService.checkExistenceById(
        criterionDBService.getRepository()::existsById, userEntityFromSave.getCriterionEntitySet());

    // Проверка на существование пользователя или администратора по email
    dbIntegrityService.checkAbsenceByUniqStr(
        userRepository::existsByEmail, userEntityFromSave.getEmail());

    // Преобразование дат рождения пользователя и рождения детей
    userEntityFromSave.setDateBirth(dateTimeService.strToDate(userSave.getDateBirth()));
    userEntityFromSave.setChildEntitySet(strBirthSetToChildEntity(userSave.getBirthDateChildren()));

    // Проверка дат рождения пользователя и детей на предшествие текущей даты
    dateTimeService.checkDateBeforeNow(userEntityFromSave.getDateBirth());
    dateTimeService.checkDateBeforeNow(userEntityFromSave.getChildEntitySet()
                           .stream().map(ChildEntity::getDateBirth).collect(Collectors.toSet()));

    userEntityFromSave.addRole(R.ROLE_USER);
    userEntityFromSave.setDateSelectCriterion(LocalDate.from(Instant.now()));
    userEntityFromSave.setFreshBenefits(false);

    userRepository.saveAndFlush(userEntityFromSave);
  }

  /**
   * Возвращает пользователя об учреждении по его ID
   * @param idUser ID пользователя
   * @return информация о пользователе
   * @throws NotFoundException если пользователь с указанным ID не найден
   */
  @Override
  public UserInfo read(String idUser) throws NotFoundException {

    String prepareIdUser = dbIntegrityService.preparePostgreSQLString(idUser);

    // Получение пользователя по его ID, если пользователь существует
    UserEntity userEntityFromRequest = userRepository.findById(prepareIdUser)
        .orElseThrow(() -> new NotFoundException(String.format(
            "User with ID \"%s\" not found", idUser)));

    // Проверка наличия роли "ROLE_USER" у пользователя
    userSecurityService.checkHasRoleElseThrowNotFound(
        userEntityFromRequest, R.ROLE_USER, R.CLIENT_USER);

    return UserDBConverter.toInfo(userEntityFromRequest);
  }

  /**
   * Обновляет пользователя по запросу на обновление
   * @param idUser ID пользователя
   * @param userSave объект запроса на сохранение пользователя
   * @throws NotFoundException если пользователь, город или критерии с указанными данными не найдены
   * @throws InvalidEmailException если указанный "email" не является email
   * @throws DateFormatException если даты рождения пользователя или детей не соответствуют формату "dd.mm.yyyy"
   * @throws DateTimeException если даты рождения пользователя или детей позже текущей даты
   * @throws AlreadyExistsException если пользователь с отличным ID и данным email уже существует
   */
  @Override
  public void update(String idUser, UserSave userSave) throws
      NotFoundException,
      InvalidEmailException,
      DateFormatException,
      DateTimeException,
      AlreadyExistsException {

    // Проверка строки email на соответствие формату email
    userSecurityService.checkEmailElseThrowInvalidEmail(
        userSave.getEmail());

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    UserEntity userEntityFromSave = UserDBConverter
        .fromSave(userSave, dbIntegrityService::preparePostgreSQLString);

    String prepareIdUser = dbIntegrityService.preparePostgreSQLString(idUser);

    // Проверка существования города и критерий по их ID
    dbIntegrityService.checkExistenceById(
        cityDBService.getRepository()::existsById, userEntityFromSave.getCityEntity());
    dbIntegrityService.checkExistenceById(
        criterionDBService.getRepository()::existsById, userEntityFromSave.getCriterionEntitySet());

    // Проверка отсутствия пользователя с отличным от данного ID и данным email
    dbIntegrityService.checkAbsenceAnotherByUniqStr(
        userRepository::existsByIdIsNotAndName, prepareIdUser, userEntityFromSave.getEmail());

    // Получение пользователя по его ID, если пользователь существует
    UserEntity userEntityFromDB = userRepository.findById(prepareIdUser)
        .orElseThrow(() -> new NotFoundException(String.format(
            "User with ID \"%s\" not found", idUser)));

    // Проверка наличия роли "ROLE_USER" у пользователя
    userSecurityService.checkHasRoleElseThrowNotFound(
        userEntityFromDB, R.ROLE_USER, R.CLIENT_USER);

    // Преобразование дат рождения пользователя и рождения детей
    userEntityFromDB.setDateBirth(dateTimeService.strToDate(
        userSave.getDateBirth()));
    userEntityFromDB.setChildEntitySet(strBirthSetToChildEntity(
        userSave.getBirthDateChildren()));

    // Проверка дат рождения пользователя и детей на предшествие текущей даты
    dateTimeService.checkDateBeforeNow(
        userEntityFromDB.getDateBirth());
    dateTimeService.checkDateBeforeNow(
        userEntityFromDB.getChildEntitySet().stream()
            .map(ChildEntity::getDateBirth).collect(Collectors.toSet()));

    userEntityFromDB.setEmail(userEntityFromSave.getEmail());
    userEntityFromDB.setName(userEntityFromSave.getName());
    userEntityFromDB.setDateSelectCriterion(LocalDate.from(Instant.now()));
    userEntityFromDB.setFreshBenefits(true);

    userRepository.saveAndFlush(userEntityFromDB);
  }

  /**
   * Удаляет пользователя по его ID или удаляет роль "ROLE_USER" у администратора
   * @param idUser ID пользователя
   * @throws NotFoundException если пользователь с указанным ID не найден
   */
  @Override
  public void delete(String idUser) throws NotFoundException {

    String prepareIdUser = dbIntegrityService.preparePostgreSQLString(idUser);

    // Получение пользователя по его ID, если пользователь существует
    UserEntity userEntityFromRequest = userRepository.findById(prepareIdUser)
        .orElseThrow(() -> new NotFoundException(String.format(
            "User with ID \"%s\" not found", idUser)));

    // Проверка наличия роли "ROLE_USER" у пользователя
    userSecurityService.checkHasRoleElseThrowNotFound(
        userEntityFromRequest, R.ROLE_USER, R.CLIENT_USER);

    // Если есть роль "ROLE_ADMIN", удаление роли "ROLE_USER", иначе удаление пользователя
    if (userEntityFromRequest.hasRole(R.ROLE_ADMIN)) {
      userEntityFromRequest.removeRole(R.ROLE_USER);
      userRepository.saveAndFlush(userEntityFromRequest);
    } else {
      userRepository.deleteById(prepareIdUser);
    }
  }

  /**
   * Возвращает дополнительные данные для пользователя.
   * Данные содержат в себе множества кратких информаций о городах и полных критериях
   * @return дополнительные данные для пользователя
   */
  @Override
  public UserInitData getInitData() {

    return UserInitData
        .builder()
        .shortCitySet(cityDBService
                          .findAllFull()
                          .stream()
                          .map(CityDBConverter::toShortInfo)
                          .collect(Collectors.toSet()))
        .criterionSet(criterionDBService
                          .findAllFull()
                          .stream()
                          .map(CriterionDBConverter::toInfo)
                          .collect(Collectors.toSet()))
        .build();
  }

  /**
   * Преобразует множество строк формата "dd.mm.yyyy" с датами рождения детей в модели таблицы "child"
   * @param strBirthSet множество строк формата "dd.mm.yyyy" с датами рождения детей
   * @return множество моделей таблицы "child"
   * @throws DateFormatException если одна из полученных строк не соответствует формату "dd.mm.yyyy"
   */
  private Set<ChildEntity> strBirthSetToChildEntity(Set<String> strBirthSet) throws DateFormatException {

    Set<LocalDate> dateBirthSet = new HashSet<>();

    // Преобразование строк формата "dd.mm.yyyy" в дату
    for (String strBirth : strBirthSet) {
      dateBirthSet.add(dateTimeService.strToDate(strBirth));
    }

    // Преобразование дат рождения в модели детей.
    // Если модели ребенка с указанной датой нет, то создается новая модель с указанной датой
    return dateBirthSet
        .stream()
        .map(dateBirth -> childRepository.findByDateBirth(dateBirth)
            .orElseGet(() -> {
              childRepository.saveAndFlush(new ChildEntity(dateBirth));
              return childRepository.getByDateBirth(dateBirth);
            }))
        .collect(Collectors.toSet());
  }
}
