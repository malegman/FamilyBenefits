package com.example.familybenefits.part_res_rest_api.services.implementations;

import com.example.familybenefits.dto.entities.ChildBirthEntity;
import com.example.familybenefits.dto.entities.UserEntity;
import com.example.familybenefits.dto.repositories.ChildBirthRepository;
import com.example.familybenefits.dto.repositories.RoleRepository;
import com.example.familybenefits.dto.repositories.UserRepository;
import com.example.familybenefits.exceptions.*;
import com.example.familybenefits.part_res_rest_api.api_model.user.UserInfo;
import com.example.familybenefits.part_res_rest_api.api_model.user.UserInitData;
import com.example.familybenefits.part_res_rest_api.api_model.user.UserSave;
import com.example.familybenefits.part_res_rest_api.converters.UserDBConverter;
import com.example.familybenefits.part_res_rest_api.services.interfaces.CityService;
import com.example.familybenefits.part_res_rest_api.services.interfaces.CriterionService;
import com.example.familybenefits.part_res_rest_api.services.interfaces.UserService;
import com.example.familybenefits.resources.R;
import com.example.familybenefits.resources.RDB;
import com.example.familybenefits.security.DBSecuritySupport;
import com.example.familybenefits.security.DateTimeSupport;
import com.example.familybenefits.security.MailSecuritySupport;
import com.example.familybenefits.security.RandomValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса, управляющего объектом "пользователь"
 */
@Slf4j
@Service
public class UserServiceFB implements UserService {

  /**
   * Репозиторий, работающий с моделью таблицы "user"
   */
  private final UserRepository userRepository;
  /**
   * Репозиторий, работающий с моделью таблицы "child"
   */
  private final ChildBirthRepository childBirthRepository;
  /**
   * Репозиторий, работающий с моделью таблицы "role"
   */
  private final RoleRepository roleRepository;

  /**
   * Интерфейс сервиса, управляющего объектом "город"
   */
  private final CityService cityService;
  /**
   * Интерфейс сервиса, управляющего объектом "критерий"
   */
  private final CriterionService criterionService;

  /**
   * Конструктор для инициализации интерфейсов репозиториев и сервисов
   * @param userRepository репозиторий, работающий с моделью таблицы "user"
   * @param childBirthRepository репозиторий, работающий с моделью таблицы "child"
   * @param roleRepository репозиторий, работающий с моделью таблицы "role"
   * @param cityService интерфейс сервиса, управляющего объектом "город"
   * @param criterionService интерфейс сервиса, управляющего объектом "критерий"
   */
  @Autowired
  public UserServiceFB(UserRepository userRepository,
                       ChildBirthRepository childBirthRepository,
                       RoleRepository roleRepository,
                       CityService cityService,
                       CriterionService criterionService) {
    this.userRepository = userRepository;
    this.childBirthRepository = childBirthRepository;
    this.roleRepository = roleRepository;
    this.cityService = cityService;
    this.criterionService = criterionService;
  }

  /**
   * Создает пользователя по запросу на сохранение. Регистрация гостя
   * @param userSave объект запроса на сохранение пользователя
   * @throws NotFoundException если город или критерии с указанными ID не найдены
   * @throws AlreadyExistsException если администратор или пользователь с указанным email уже существует
   * @throws InvalidEmailException если указанный "email" не является email
   * @throws DateFormatException если даты рождения пользователя или детей не соответствуют формату "dd.mm.yyyy"
   * @throws DateTimeException если даты рождения пользователя или детей позже текущей даты
   * @throws InvalidStringException если строковое поле объекта запроса не содержит букв или цифр
   */
  @Override
  public void create(UserSave userSave) throws
      NotFoundException,
      AlreadyExistsException,
      InvalidEmailException,
      DateFormatException,
      DateTimeException,
      InvalidStringException{

    // Проверка строки email на соответствие формату email
    MailSecuritySupport.checkEmailElseThrow(userSave.getEmail());

    // Проверка существования города и критерий по их ID
    DBSecuritySupport.checkExistenceById(cityService::existsById, userSave.getIdCity());
    DBSecuritySupport.checkExistenceById(criterionService::existsById, userSave.getIdCriterionList());

    // Проверка на отсутствие пользователя или администратора по email
    DBSecuritySupport.checkAbsenceByUniqStr(userRepository::existsByEmail, userSave.getEmail());

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    UserEntity userEntityFromSave = UserDBConverter
        .fromSave(null, userSave, DBSecuritySupport::preparePostgreSQLString);
    String idUser = userEntityFromSave.getId();

    // Преобразование дат рождения пользователя и рождения детей
    userEntityFromSave.setDateBirth(DateTimeSupport.strToDate(userSave.getDateBirth()));
    List<LocalDate> childBirthList = DateTimeSupport.strToDate(userSave.getBirthDateChildren());

    // Проверка дат рождения пользователя и детей на предшествие текущей даты
    DateTimeSupport.checkDateBeforeNow(userEntityFromSave.getDateBirth());
    DateTimeSupport.checkDateBeforeNow(childBirthList);

    userRepository.save(userEntityFromSave);
    userRepository.addRole(idUser, RDB.ID_ROLE_USER);
    setChildrenToUser(idUser, childBirthList);
    setCriteriaToUser(idUser, userSave.getIdCriterionList());

    log.info("DB. User with email \"{}\" created.", userSave.getEmail());
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
    UserEntity userEntityFromRequest = getUserEntity(idUser);

    return UserDBConverter.toInfo(userEntityFromRequest,
                                  childBirthRepository.findAllByIdUser(idUser),
                                  roleRepository.findAllByIdUser(idUser),
                                  cityService.readName(userEntityFromRequest.getIdCity()));
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
   * @throws InvalidStringException если строковое поле объекта запроса не содержит букв или цифр
   */
  @Override
  public void update(String idUser, UserSave userSave) throws
      NotFoundException,
      InvalidEmailException,
      DateFormatException,
      DateTimeException,
      AlreadyExistsException,
      InvalidStringException {

    // Проверка строки email на соответствие формату email
    MailSecuritySupport.checkEmailElseThrow(userSave.getEmail());

    // Проверка отсутствия пользователя с отличным от данного ID и данным email
    DBSecuritySupport.checkAbsenceAnotherByUniqStr(
        userRepository::existsByIdIsNotAndEmail, idUser, userSave.getEmail());

    // Проверка существования города и критерий по их ID
    DBSecuritySupport.checkExistenceById(cityService::existsById, userSave.getIdCity());
    DBSecuritySupport.checkExistenceById(criterionService::existsById, userSave.getIdCriterionList());

    // Получение пользователя по его ID, если пользователь существует
    UserEntity userEntityFromDB = getUserEntity(idUser);

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    UserEntity userEntityFromSave = UserDBConverter
        .fromSave(idUser, userSave, DBSecuritySupport::preparePostgreSQLString);

    // Преобразование дат рождения пользователя и рождения детей
    userEntityFromDB.setDateBirth(DateTimeSupport.strToDate(userSave.getDateBirth()));
    List<LocalDate> childBirthList = DateTimeSupport.strToDate(userSave.getBirthDateChildren());

    // Проверка дат рождения пользователя и детей на предшествие текущей даты
    DateTimeSupport.checkDateBeforeNow(userEntityFromDB.getDateBirth());
    DateTimeSupport.checkDateBeforeNow(childBirthList);

    userEntityFromDB.setEmail(userEntityFromSave.getEmail());
    userEntityFromDB.setName(userEntityFromSave.getName());

    userRepository.save(userEntityFromDB);
    setChildrenToUser(idUser, childBirthList);
    setCriteriaToUser(idUser, userSave.getIdCriterionList());

    log.info("DB. User with ID \"{}\" updated.", idUser);
  }

  /**
   * Удаляет пользователя по его ID или удаляет роль "ROLE_USER" у администратора
   * @param idUser ID пользователя
   * @throws NotFoundException если пользователь с указанным ID не найден
   */
  @Override
  public void delete(String idUser) throws NotFoundException {

    // Проверка существования пользователя по ID
    DBSecuritySupport.checkExistenceById(userRepository::existsById, idUser);

    // Если есть роль "ROLE_ADMIN", удаление роли "ROLE_USER", иначе удаление пользователя и его токена восстановления
    if (userRepository.hasUserRole(idUser, RDB.ID_ROLE_ADMIN)) {
      userRepository.deleteRole(idUser, RDB.ID_ROLE_ADMIN);
      log.info("DB. User with ID \"{}\" updated. Removed role \"{}\"", idUser, RDB.NAME_ROLE_USER);
    } else {
      userRepository.deleteById(idUser);
      log.info("DB. User with ID \"{}\" deleted.", idUser);
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
        .shortCityList(cityService.readAllFullShort())
        .criterionList(criterionService.readAllFull())
        .build();
  }

  /**
   * Проверяет существование пользователя по его ID
   * @param idUser ID пользователя, предварительно обработанный
   * @return true, если пользователь найден
   */
  @Override
  public boolean existsById(String idUser) {

    return userRepository.existsById(idUser);
  }

  /**
   * Возвращает модель пользователя по его ID
   * @param idUser ID пользователя
   * @return модель пользователя
   * @throws NotFoundException если пользователь не найден
   */
  private UserEntity getUserEntity(String idUser) throws NotFoundException {

    String preparedId = DBSecuritySupport.preparePostgreSQLString(idUser);
    return userRepository.findById(preparedId).orElseThrow(
        () -> new NotFoundException(String.format("User with ID \"%s\" not found", preparedId)));
  }

  /**
   * Добавляет даты рождения детей указанному пользователю по его ID
   * @param idUser ID пользователя, которому добавляются даты рождения детей
   * @param childBirthList список дат рождений детей
   */
  private void setChildrenToUser(String idUser, List<LocalDate> childBirthList) {

    userRepository.deleteAllChildBirths(idUser);

    for (LocalDate childBirth : childBirthList) {

      // Получение существующей модели рождения ребенка или же создание новой
      Optional<ChildBirthEntity> childEntityOpt = childBirthRepository.findByDateBirth(childBirth);

      if (childEntityOpt.isPresent()) {
        userRepository.addChild(idUser, childEntityOpt.get().getId());

      } else {
        ChildBirthEntity newChildBirthEntity = new ChildBirthEntity(RandomValue.randomString(R.ID_LENGTH), childBirth);
        childBirthRepository.save(newChildBirthEntity);
        userRepository.addChild(idUser, newChildBirthEntity.getId());
      }
    }
  }

  /**
   * Добавляет критерии указанному пользователю по его ID
   * @param idUser ID пользователя, которому добавляются критерии
   * @param idCriterionList список ID добавляемых критериев
   */
  private void setCriteriaToUser(String idUser, List<String> idCriterionList) {

    userRepository.deleteAllCriteria(idUser);
    idCriterionList.forEach(idCriterion -> userRepository.addCriterion(idUser, idCriterion));
  }
}
