package com.example.familybenefits.service.implementation;

import com.example.familybenefits.api_model.benefit.BenefitInfo;
import com.example.familybenefits.api_model.user.UserAdd;
import com.example.familybenefits.api_model.user.UserInfo;
import com.example.familybenefits.api_model.user.UserInitData;
import com.example.familybenefits.api_model.user.UserUpdate;
import com.example.familybenefits.convert.BenefitConverter;
import com.example.familybenefits.convert.CityConverter;
import com.example.familybenefits.convert.CriterionConverter;
import com.example.familybenefits.convert.UserConverter;
import com.example.familybenefits.dao.entity.*;
import com.example.familybenefits.dao.repository.ChildRepository;
import com.example.familybenefits.dao.repository.UserRepository;
import com.example.familybenefits.exception.*;
import com.example.familybenefits.security.service.s_interface.DBIntegrityService;
import com.example.familybenefits.security.service.s_interface.UserSecurityService;
import com.example.familybenefits.service.s_interface.PartEntityService;
import com.example.familybenefits.service.s_interface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
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
   * Интерфейс сервиса для моделей таблицы "benefit", целостность которых зависит от связанных таблиц
   */
  private final PartEntityService<BenefitEntity> benefitPartEntityService;

  /**
   * Интерфейс сервиса для моделей таблицы "city", целостность которых зависит от связанных таблиц
   */
  private final PartEntityService<CityEntity> cityPartEntityService;

  /**
   * Интерфейс сервиса для моделей таблицы "criterion", целостность которых зависит от связанных таблиц
   */
  private final PartEntityService<CriterionEntity> criterionPartEntityService;

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
   * @param benefitPartEntityService интерфейс сервиса для моделей таблицы "benefit", целостность которых зависит от связанных таблиц
   * @param cityPartEntityService интерфейс сервиса для моделей таблицы "city", целостность которых зависит от связанных таблиц
   * @param criterionPartEntityService интерфейс сервиса для моделей таблицы "criterion", целостность которых зависит от связанных таблиц
   * @param dbIntegrityService интерфейс сервиса, отвечающего за целостность базы данных
   * @param userSecurityService интерфейс сервиса, отвечающего за данные пользователя
   * @param passwordEncoder интерфейс сервиса для шифрования паролей
   */
  @Autowired
  public UserServiceFB(UserRepository userRepository,
                       ChildRepository childRepository,
                       PartEntityService<BenefitEntity> benefitPartEntityService,
                       PartEntityService<CityEntity> cityPartEntityService,
                       PartEntityService<CriterionEntity> criterionPartEntityService,
                       DBIntegrityService dbIntegrityService,
                       UserSecurityService userSecurityService,
                       PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.childRepository = childRepository;
    this.benefitPartEntityService = benefitPartEntityService;
    this.cityPartEntityService = cityPartEntityService;
    this.criterionPartEntityService = criterionPartEntityService;
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
   * @throws PasswordNotEqualsException если указанные пароли не эквивалентны
   * @throws InvalidEmailException если указанный "email" не является email
   * @throws DateFormatException если даты рождения пользователя или детей не соответствуют формату "dd.mm.yyyy"
   * @throws DateTimeException если даты рождения пользователя или детей позже текущей даты
   */
  @Override
  public void add(UserAdd userAdd) throws
      NotFoundException,
      AlreadyExistsException,
      PasswordNotSafetyException,
      PasswordNotEqualsException,
      InvalidEmailException,
      DateFormatException,
      DateTimeException {

    // Проверка существования города и критерий по их ID
    dbIntegrityService.checkExistenceByIdElseThrowNotFound(
        cityPartEntityService::existsById, userAdd.getIdCity(),
        "City with ID %s not found");
    dbIntegrityService.checkExistenceByIdElseThrowNotFound(
        criterionPartEntityService::existsById, userAdd.getIdCriterionSet(),
        "Criterion with ID %s not found");

    // Проверка паролей на эквивалентность и безопасность
    userSecurityService.checkPasswordElseThrow(
        userAdd.getPassword(), userAdd.getRepeatPassword(),
        "Input passwords are not equals",
        "Input password is not safety");

    // Проверка строки email на соответствие формату email
    userSecurityService.checkEmailElseThrowInvalidEmail(
        userAdd.getEmail(),
        "Input value is not an email");

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    UserEntity userEntityFromAdd = (UserEntity) UserConverter
        .fromAdd(userAdd)
        .prepareForDB(dbIntegrityService::preparePostgreSQLString);

    // Проверка на существование пользователя или администратора по email
    dbIntegrityService.checkAbsenceByUniqStrElseThrowAlreadyExists(
        userRepository::existsByEmail, userEntityFromAdd.getEmail(),
        "User or administrator with email %s already exists");

    // Преобразование дат рождения пользователя и рождения детей
    userEntityFromAdd.setDateBirth(strToDateElseThrow(
        userAdd.getDateBirth(),
        "User's birthday %s doesn't match the date format \"dd.mm.yyyy\""));
    userEntityFromAdd.setChildEntitySet(strBirthSetToChildEntity(
        userAdd.getBirthDateChildren(),
        "Child's birthday %s doesn't match the date format \"dd.mm.yyyy\""));

    // Проверка дат рождения пользователя и детей на предшествие текущей даты
    checkDateBeforeNow(
        userEntityFromAdd.getDateBirth(),
        "User's birthday %s is after now");
    checkDateBeforeNow(
        userEntityFromAdd.getChildEntitySet().stream()
            .map(ChildEntity::getDateBirth).collect(Collectors.toSet()),
        "Child's birthday %s is after now");

    userEntityFromAdd.addRole("ROLE_USER");
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

    // Проверка существования города и критерий по их ID
    dbIntegrityService.checkExistenceByIdElseThrowNotFound(
        cityPartEntityService::existsById, userUpdate.getIdCity(),
        "City with ID %s not found");
    dbIntegrityService.checkExistenceByIdElseThrowNotFound(
        criterionPartEntityService::existsById, userUpdate.getIdCriterionSet(),
        "Criterion with ID %s not found");

    // Проверка строки email на соответствие формату email
    userSecurityService.checkEmailElseThrowInvalidEmail(
        userUpdate.getEmail(),
        "Input value is not an email");

    // Получение пользователя по его ID, если пользователь существует
    UserEntity userEntityFromDB = userRepository.findById(userUpdate.getId())
        .orElseThrow(() -> new NotFoundException(String.format(
            "User with ID %s not found", userUpdate.getId())));

    // Проверка наличия роли "ROLE_USER" у пользователя
    userSecurityService.checkHasRoleElseThrowNotFound(
        userEntityFromDB, "ROLE_USER",
        "User with ID %s not found");

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    UserEntity userEntityFromUpdate = (UserEntity) UserConverter
        .fromUpdate(userUpdate)
        .prepareForDB(dbIntegrityService::preparePostgreSQLString);

    // Изменение email и сброс его подтверждения, если новое email
    if (!userEntityFromUpdate.getEmail().equals(userEntityFromDB.getEmail())) {
      userEntityFromDB.setVerifiedEmail(false);
      userEntityFromDB.setEmail(userEntityFromUpdate.getEmail());
    }

    // Преобразование дат рождения пользователя и рождения детей
    userEntityFromUpdate.setDateBirth(strToDateElseThrow(
        userUpdate.getDateBirth(),
        "User's birthday %s doesn't match the date format \"dd.mm.yyyy\""));
    userEntityFromUpdate.setChildEntitySet(strBirthSetToChildEntity(
        userUpdate.getBirthDateChildren(),
        "Child's birthday %s doesn't match the date format \"dd.mm.yyyy\""));

    // Проверка дат рождения пользователя и детей на предшествие текущей даты
    checkDateBeforeNow(
        userEntityFromUpdate.getDateBirth(),
        "User's birthday %s is after now");
    checkDateBeforeNow(
        userEntityFromUpdate.getChildEntitySet().stream()
            .map(ChildEntity::getDateBirth).collect(Collectors.toSet()),
        "Child's birthday %s is after now");

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
  public void delete(BigInteger idUser) throws NotFoundException {

    // Получение пользователя по его ID, если пользователь существует
    UserEntity userEntityFromRequest = userRepository.findById(idUser)
        .orElseThrow(() -> new NotFoundException(String.format(
            "User with ID %s not found", idUser)));

    // Проверка наличия роли "ROLE_USER" у пользователя
    userSecurityService.checkHasRoleElseThrowNotFound(
        userEntityFromRequest, "ROLE_USER",
        "User with ID %s not found");

    // Если есть роль "ROLE_ADMIN", удаление роли "ROLE_USER", иначе удаление пользователя
    if (userEntityFromRequest.hasRole("ROLE_ADMIN")) {
      userEntityFromRequest.removeRole("ROLE_USER");
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
  public UserInfo read(BigInteger idUser) throws NotFoundException {

    // Получение пользователя по его ID, если пользователь существует
    UserEntity userEntityFromRequest = userRepository.findById(idUser)
        .orElseThrow(() -> new NotFoundException(String.format(
            "User with ID %s not found", idUser)));

    // Проверка наличия роли "ROLE_USER" у пользователя
    userSecurityService.checkHasRoleElseThrowNotFound(
        userEntityFromRequest, "ROLE_USER",
        "User with ID %s not found");

    return UserConverter.toInfo(userEntityFromRequest);
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
        .shortCitySet(cityPartEntityService
                          .findAllFull()
                          .stream()
                          .map(CityConverter::toShortInfo)
                          .collect(Collectors.toSet()))
        .criterionSet(criterionPartEntityService
                          .findAllFull()
                          .stream()
                          .map(CriterionConverter::toInfo)
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
  public Set<BenefitInfo> getBenefits(BigInteger idUser) throws NotFoundException, DateTimeException {

    // Получение пользователя по его ID, если пользователь существует
    UserEntity userEntityFromRequest = userRepository.findById(idUser)
        .orElseThrow(() -> new NotFoundException(String.format(
            "User with ID %s not found", idUser)));

    // Проверка наличия роли "ROLE_USER" у пользователя
    userSecurityService.checkHasRoleElseThrowNotFound(
        userEntityFromRequest, "ROLE_USER",
        "User with ID %s not found");
    LocalDate localDateCriterion = userEntityFromRequest.getDateSelectCriterion();

    // Проверка разницы дат между текущей датой и датой последней установки критериев
    // относительно дат рождений пользователя и его детей
    checkHasBirthdayAfterElseThrow(
        userEntityFromRequest.getDateBirth(),
        localDateCriterion,
        String.format("Need to update criteria. The user with ID %s had birthday", idUser));
    checkHasBirthdayAfterElseThrow(
        userEntityFromRequest.getChildEntitySet().stream()
            .map(ChildEntity::getDateBirth).collect(Collectors.toSet()),
        localDateCriterion,
        "Need to update criteria. The user with ID %s had child's birthday");

    // Если пособия пользователя не свежие, то
    // подбираются пособия, критерии которых включают в себя все критерии пользователя,
    // обновляется флаг свежести пособий и внесенные изменения сохраняются
    if (!userEntityFromRequest.isFreshBenefits()) {
      Set<CriterionEntity> userCriteria = userEntityFromRequest.getCriterionEntitySet();

      userEntityFromRequest.setBenefitEntitySet(
          benefitPartEntityService
              .findAllFull()
              .stream()
              .filter(benefitEntity -> benefitEntity.getCriterionEntitySet().containsAll(userCriteria))
              .collect(Collectors.toSet()));
      userEntityFromRequest.setFreshBenefits(true);

      userRepository.saveAndFlush(userEntityFromRequest);
    }

    return userEntityFromRequest.getBenefitEntitySet()
        .stream()
        .map(BenefitConverter::toInfo)
        .collect(Collectors.toSet());
  }

  /**
   * Преобразует строку формата "dd.mm.yyyy" в дату
   * @param userBirth дата в строковом виде
   * @param messagePattern шаблон сообщения об ошибке
   * @return преобразованная строка в формат даты
   * @throws DateFormatException если полученная строка не соответствует формату "dd.mm.yyyy"
   */
  private LocalDate strToDateElseThrow(String userBirth, String messagePattern) throws DateFormatException {

    try {
      return LocalDate.from((TemporalAccessor) SIMPLE_DATE_FORMAT.parse(userBirth));
    } catch (ParseException e) {
      throw new DateFormatException(String.format(messagePattern, userBirth));
    }
  }

  /**
   * Преобразует множество строк формата "dd.mm.yyyy" с датами рождения детей в модели таблицы "child"
   * @param strBirthSet множество строк формата "dd.mm.yyyy" с датами рождения детей
   * @param messagePattern шаблон сообщения об ошибке
   * @return множество моделей таблицы "child"
   * @throws DateFormatException если одна из полученных строк не соответствует формату "dd.mm.yyyy"
   */
  private Set<ChildEntity> strBirthSetToChildEntity(Set<String> strBirthSet, String messagePattern) throws DateFormatException {

    Set<LocalDate> dateBirthSet = new HashSet<>();

    // Преобразование строк формата "dd.mm.yyyy" в дату
    for (String strBirth : strBirthSet) {
      dateBirthSet.add(strToDateElseThrow(strBirth, messagePattern));
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
   * @param date проверяемая дата
   * @param messagePattern шаблон сообщения об ошибке
   * @throws DateTimeException если проверяемая дата позже текущей даты
   */
  private void checkDateBeforeNow(LocalDate date, String messagePattern) throws DateTimeException {

    if (date.isAfter(LocalDate.now())) {
      throw new DateTimeException(String.format(messagePattern, date));
    }
  }

  /**
   * Проверяет множество дат на предшествие текущей дате
   * @param dateSet множество проверяемых дат
   * @param messagePattern шаблон сообщения об ошибке
   * @throws DateTimeException если проверяемая дата позже текущей даты
   */
  private void checkDateBeforeNow(Set<LocalDate> dateSet, String messagePattern) throws DateTimeException {

    for (LocalDate date : dateSet) {
      checkDateBeforeNow(date, messagePattern);
    }
  }

  /**
   * Проверяет разницу между двумя датами относительно базовой даты
   * @param dateBase дата, относительно которой сравниваются даты
   * @param dateDiff1 первая дата для разницы
   * @param message сообщение об ошибке
   * @throws DateTimeException если даты разницы отличаются друг от друга относительно базовой даты на год и больше
   */
  private void checkHasBirthdayAfterElseThrow(LocalDate dateBase, LocalDate dateDiff1, String message) throws DateTimeException {

    if (Period.between(dateDiff1, dateBase).getYears() != Period.between(LocalDate.now(), dateBase).getYears()) {
      throw new DateTimeException(message);
    }
  }

  /**
   * Проверяет разницу между двумя датами относительно базовой даты
   * @param dateBaseSet множество дат, относительно которых сравниваются даты
   * @param dateDiff1 первая дата для разницы
   * @param message сообщение об ошибке
   * @throws DateTimeException если даты разницы отличаются друг от друга относительно базовой даты на год и больше
   */
  private void checkHasBirthdayAfterElseThrow(Set<LocalDate> dateBaseSet, LocalDate dateDiff1, String message) throws DateTimeException {

    for (LocalDate dateBase : dateBaseSet) {
      checkHasBirthdayAfterElseThrow(dateBase, dateDiff1, message);
    }
  }
}
