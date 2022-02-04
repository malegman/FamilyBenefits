package com.example.familybenefits.service.implementation;

import com.example.familybenefits.api_model.benefit.BenefitInfo;
import com.example.familybenefits.api_model.user.UserAdd;
import com.example.familybenefits.api_model.user.UserInfo;
import com.example.familybenefits.api_model.user.UserInitData;
import com.example.familybenefits.api_model.user.UserUpdate;
import com.example.familybenefits.convert.BenefitDBConverter;
import com.example.familybenefits.convert.CityDBConverter;
import com.example.familybenefits.convert.CriterionDBConverter;
import com.example.familybenefits.convert.UserDBConverter;
import com.example.familybenefits.dao.entity.*;
import com.example.familybenefits.dao.repository.*;
import com.example.familybenefits.exception.*;
import com.example.familybenefits.resource.R;
import com.example.familybenefits.security.service.s_interface.DBIntegrityService;
import com.example.familybenefits.security.service.s_interface.UserSecurityService;
import com.example.familybenefits.service.s_interface.EntityDBService;
import com.example.familybenefits.service.s_interface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Реализация сервиса, управляющего объектом "пользователь"
 */
@Service
public class UserServiceFB implements UserService {

  /**
   * Формат даты для преобразования строки в дату и дату в строку
   */
  private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy", Locale.US);

  /**
   * Репозиторий, работающий с моделью таблицы "user"
   */
  private final UserRepository userRepository;

  /**
   * Репозиторий, работающий с моделью таблицы "child"
   */
  private final ChildRepository childRepository;

  /**
   * Интерфейс сервиса модели таблицы "benefit", целостность которой зависит от связанных таблиц
   */
  private final EntityDBService<BenefitEntity, BenefitRepository> benefitDBService;
  /**
   * Интерфейс сервиса модели таблицы "city", целостность которой зависит от связанных таблиц
   */
  private final EntityDBService<CityEntity, CityRepository> cityDBService;
  /**
   * Интерфейс сервиса модели таблицы "criterion", целостность которой зависит от связанных таблиц
   */
  private final EntityDBService<CriterionEntity, CriterionRepository> criterionDBService;

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
   * @param childRepository репозиторий, работающий с моделью таблицы "child"
   * @param benefitDBService интерфейс сервиса модели таблицы "benefit", целостность которой зависит от связанных таблиц
   * @param cityDBService интерфейс сервиса модели таблицы "city", целостность которой зависит от связанных таблиц
   * @param criterionDBService интерфейс сервиса модели таблицы "criterion", целостность которой зависит от связанных таблиц
   * @param dbIntegrityService интерфейс сервиса, отвечающего за целостность базы данных
   * @param userSecurityService интерфейс сервиса, отвечающего за данные пользователя
   * @param passwordEncoder интерфейс сервиса для шифрования паролей
   */
  @Autowired
  public UserServiceFB(UserRepository userRepository,
                       ChildRepository childRepository,
                       EntityDBService<BenefitEntity, BenefitRepository> benefitDBService,
                       EntityDBService<CityEntity, CityRepository> cityDBService,
                       EntityDBService<CriterionEntity, CriterionRepository> criterionDBService,
                       DBIntegrityService dbIntegrityService,
                       UserSecurityService userSecurityService,
                       PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.childRepository = childRepository;
    this.benefitDBService = benefitDBService;
    this.cityDBService = cityDBService;
    this.criterionDBService = criterionDBService;
    this.dbIntegrityService = dbIntegrityService;
    this.userSecurityService = userSecurityService;
    this.passwordEncoder = passwordEncoder;
  }

  /**
   * Добавляет нового пользователя. Регистрация гостя
   * @param userAdd объект запроса на добавление пользователя
   * @throws NotFoundException если город или критерии с указанными данными не найдены
   * @throws AlreadyExistsException если администратор или пользователь с указанным email уже существует
   * @throws PasswordNotSafetyException если пароль не соответствует политике безопасности
   * @throws NotEqualException если указанные пароли не эквивалентны
   * @throws InvalidEmailException если указанный "email" не является email
   * @throws DateFormatException если даты рождения пользователя или детей не соответствуют формату "dd.mm.yyyy"
   * @throws DateTimeException если даты рождения пользователя или детей позже текущей даты
   */
  @Override
  public void add(UserAdd userAdd) throws
      NotFoundException,
      AlreadyExistsException,
      PasswordNotSafetyException,
      NotEqualException,
      InvalidEmailException,
      DateFormatException,
      DateTimeException {

    // Проверка паролей на эквивалентность и безопасность
    userSecurityService.checkPasswordElseThrow(
        userAdd.getPassword(), userAdd.getRepeatPassword());

    // Проверка строки email на соответствие формату email
    userSecurityService.checkEmailElseThrowInvalidEmail(
        userAdd.getEmail());

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    UserEntity userEntityFromAdd = UserDBConverter
        .fromAdd(userAdd, dbIntegrityService::preparePostgreSQLString);

    // Проверка существования города и критерий по их ID
    dbIntegrityService.checkExistenceById(
        cityDBService.getRepository()::existsById, userEntityFromAdd.getCityEntity());
    dbIntegrityService.checkExistenceById(
        criterionDBService.getRepository()::existsById, userEntityFromAdd.getCriterionEntitySet());

    // Проверка на существование пользователя или администратора по email
    dbIntegrityService.checkAbsenceByUniqStr(
        userRepository::existsByEmail, userEntityFromAdd.getEmail());

    // Преобразование дат рождения пользователя и рождения детей
    userEntityFromAdd.setDateBirth(strToDate(userAdd.getDateBirth()));
    userEntityFromAdd.setChildEntitySet(strBirthSetToChildEntity(userAdd.getBirthDateChildren()));

    // Проверка дат рождения пользователя и детей на предшествие текущей даты
    checkDateBeforeNow(userEntityFromAdd.getDateBirth());
    checkDateBeforeNow(userEntityFromAdd.getChildEntitySet()
                           .stream().map(ChildEntity::getDateBirth).collect(Collectors.toSet()));

    userEntityFromAdd.addRole(R.ROLE_USER);
    userEntityFromAdd.setVerifiedEmail(false);
    userEntityFromAdd.encryptPassword(passwordEncoder::encode);
    userEntityFromAdd.setDateSelectCriterion(LocalDate.from(Instant.now()));
    userEntityFromAdd.setFreshBenefits(false);

    userRepository.saveAndFlush(userEntityFromAdd);
  }

  /**
   * Обновляет пользователя по запросу на обновление
   * @param userUpdate объект запроса на обновление пользователя
   * @throws NotFoundException если пользователь, город или критерии с указанными данными не найдены
   * @throws InvalidEmailException если указанный "email" не является email
   * @throws DateFormatException если даты рождения пользователя или детей не соответствуют формату "dd.mm.yyyy"
   * @throws DateTimeException если даты рождения пользователя или детей позже текущей даты
   */
  @Override
  public void update(UserUpdate userUpdate) throws
      NotFoundException,
      InvalidEmailException,
      DateFormatException,
      DateTimeException {

    // Проверка строки email на соответствие формату email
    userSecurityService.checkEmailElseThrowInvalidEmail(
        userUpdate.getEmail());

    // Получение пользователя по его ID, если пользователь существует
    UserEntity userEntityFromDB = userRepository.findById(userUpdate.getId())
        .orElseThrow(() -> new NotFoundException(String.format(
            "User with ID \"%s\" not found", userUpdate.getId())));

    // Проверка существования города и критерий по их ID
    dbIntegrityService.checkExistenceById(
        cityDBService.getRepository()::existsById, userEntityFromDB.getCityEntity());
    dbIntegrityService.checkExistenceById(
        criterionDBService.getRepository()::existsById, userEntityFromDB.getCriterionEntitySet());

    // Проверка наличия роли "ROLE_USER" у пользователя
    userSecurityService.checkHasRoleElseThrowNotFound(
        userEntityFromDB, R.ROLE_USER, R.CLIENT_USER);

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    UserEntity userEntityFromUpdate = UserDBConverter
        .fromUpdate(userUpdate, dbIntegrityService::preparePostgreSQLString);

    // Изменение email и сброс его подтверждения, если новое email
    if (!userEntityFromUpdate.getEmail().equals(userEntityFromDB.getEmail())) {
      userEntityFromDB.setVerifiedEmail(false);
      userEntityFromDB.setEmail(userEntityFromUpdate.getEmail());
    }

    // Преобразование дат рождения пользователя и рождения детей
    userEntityFromDB.setDateBirth(strToDate(
        userUpdate.getDateBirth()));
    userEntityFromDB.setChildEntitySet(strBirthSetToChildEntity(
        userUpdate.getBirthDateChildren()));

    // Проверка дат рождения пользователя и детей на предшествие текущей даты
    checkDateBeforeNow(
        userEntityFromUpdate.getDateBirth());
    checkDateBeforeNow(
        userEntityFromUpdate.getChildEntitySet().stream()
            .map(ChildEntity::getDateBirth).collect(Collectors.toSet()));

    userEntityFromDB.setName(userEntityFromUpdate.getName());
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

    // Получение пользователя по его ID, если пользователь существует
    UserEntity userEntityFromRequest = userRepository.findById(idUser)
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
      userRepository.deleteById(idUser);
    }
  }

  /**
   * Возвращает пользователя об учреждении по его ID
   * @param idUser ID пользователя
   * @return информация о пользователе
   * @throws NotFoundException если пользователь с указанным ID не найден
   */
  @Override
  public UserInfo read(String idUser) throws NotFoundException {

    // Получение пользователя по его ID, если пользователь существует
    UserEntity userEntityFromRequest = userRepository.findById(idUser)
        .orElseThrow(() -> new NotFoundException(String.format(
            "User with ID \"%s\" not found", idUser)));

    // Проверка наличия роли "ROLE_USER" у пользователя
    userSecurityService.checkHasRoleElseThrowNotFound(
        userEntityFromRequest, R.ROLE_USER, R.CLIENT_USER);

    return UserDBConverter.toInfo(userEntityFromRequest);
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
   * Возвращает множество подобранных пособий для пользователя
   * @param idUser ID пользователя
   * @return множество подобранных пособий
   * @throws NotFoundException если пользователь с указанным ID не найден
   * @throws DateTimeException если критерии пользователя устарели
   */
  @Override
  public Set<BenefitInfo> getBenefits(String idUser) throws NotFoundException, DateTimeException {

    // Получение пользователя по его ID, если пользователь существует
    UserEntity userEntityFromRequest = userRepository.findById(idUser)
        .orElseThrow(() -> new NotFoundException(String.format(
            "User with ID \"%s\" not found", idUser)));

    // Проверка наличия роли "ROLE_USER" у пользователя
    userSecurityService.checkHasRoleElseThrowNotFound(
        userEntityFromRequest, R.ROLE_USER, R.CLIENT_USER);

    LocalDate localDateCriterion = userEntityFromRequest.getDateSelectCriterion();
    // Проверка разницы дат между текущей датой и датой последней установки критериев
    // относительно дат рождений пользователя и его детей
    checkBirthdayBeforeElseThrow(
        userEntityFromRequest.getDateBirth(), localDateCriterion);
    checkBirthdayBeforeElseThrow(
        userEntityFromRequest.getChildEntitySet().stream()
            .map(ChildEntity::getDateBirth).collect(Collectors.toSet()), localDateCriterion);

    // Если пособия пользователя не свежие, то
    // подбираются пособия, критерии которых включают в себя все критерии пользователя,
    // обновляется флаг свежести пособий и внесенные изменения сохраняются
    if (!userEntityFromRequest.isFreshBenefits()) {
      Set<CriterionEntity> userCriteria = userEntityFromRequest.getCriterionEntitySet();

      userEntityFromRequest.setBenefitEntitySet(
          benefitDBService
              .findAllFull()
              .stream()
              .filter(benefitEntity -> benefitEntity.getCriterionEntitySet().containsAll(userCriteria))
              .collect(Collectors.toSet()));
      userEntityFromRequest.setFreshBenefits(true);

      userRepository.saveAndFlush(userEntityFromRequest);
    }

    return userEntityFromRequest.getBenefitEntitySet()
        .stream()
        .map(BenefitDBConverter::toInfo)
        .collect(Collectors.toSet());
  }

  /**
   * Преобразует строку формата "dd.mm.yyyy" в дату
   * @param userBirth дата в строковом виде
   * @return преобразованная строка в формат даты
   * @throws DateFormatException если полученная строка не соответствует формату "dd.mm.yyyy"
   */
  private LocalDate strToDate(String userBirth) throws DateFormatException {

    try {
      return LocalDate.from((TemporalAccessor) SIMPLE_DATE_FORMAT.parse(userBirth));
    } catch (ParseException e) {
      throw new DateFormatException(String.format(
          "The string \"%s\" doesn't match the date format \"dd.mm.yyyy\"", userBirth));
    }
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
      dateBirthSet.add(strToDate(strBirth));
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

  /**
   * Проверяет дату на предшествие текущей дате
   * @param dateCheck проверяемая дата
   * @throws DateTimeException если проверяемая дата позже текущей даты
   */
  private void checkDateBeforeNow(LocalDate dateCheck) throws DateTimeException {

    LocalDate dateCurrent = LocalDate.now();
    if (dateCheck.isAfter(dateCurrent)) {
      throw new DateTimeException(String.format(
          "The date \"%s\" is after current date \"%s\"", dateCheck, dateCurrent));
    }
  }

  /**
   * Проверяет множество дат на предшествие текущей дате
   * @param dateSet множество проверяемых дат
   * @throws DateTimeException если проверяемая дата позже текущей даты
   */
  private void checkDateBeforeNow(Set<LocalDate> dateSet) throws DateTimeException {

    for (LocalDate date : dateSet) {
      checkDateBeforeNow(date);
    }
  }

  /**
   * Проверяет, был ли день рождения после проверяемой даты
   * @param dateBirth дата рождения
   * @param dateCheck проверяемая дата
   * @throws DateTimeException если день рождения был после проверяемой даты
   */
  private void checkBirthdayBeforeElseThrow(LocalDate dateBirth, LocalDate dateCheck) throws DateTimeException {

    if (dateBirth.plusYears(LocalDate.now().getYear() - dateBirth.getYear()).isAfter(dateCheck)) {
      throw new DateTimeException(String.format(
          "The day of birthday of date \"%s\" was after check date \"%s\"", dateBirth, dateCheck));
    }
  }

  /**
   * Проверяет, был ли дни рождения после проверяемой даты
   * @param dateBirthSet множество дат рождения
   * @param dateCheck проверяемая дата
   * @throws DateTimeException если один из дней рождения был после проверяемой даты
   */
  private void checkBirthdayBeforeElseThrow(Set<LocalDate> dateBirthSet, LocalDate dateCheck) throws DateTimeException {

    for (LocalDate dateBase : dateBirthSet) {
      checkBirthdayBeforeElseThrow(dateBase, dateCheck);
    }
  }
}
