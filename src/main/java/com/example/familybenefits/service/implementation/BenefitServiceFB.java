package com.example.familybenefits.service.implementation;

import com.example.familybenefits.api_model.benefit.BenefitInfo;
import com.example.familybenefits.api_model.benefit.BenefitInitData;
import com.example.familybenefits.api_model.benefit.BenefitSave;
import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.example.familybenefits.convert.BenefitDBConverter;
import com.example.familybenefits.convert.CityDBConverter;
import com.example.familybenefits.convert.CriterionDBConverter;
import com.example.familybenefits.dto.entity.*;
import com.example.familybenefits.dto.repository.BenefitRepository;
import com.example.familybenefits.dto.repository.CityRepository;
import com.example.familybenefits.dto.repository.CriterionRepository;
import com.example.familybenefits.dto.repository.UserRepository;
import com.example.familybenefits.exception.AlreadyExistsException;
import com.example.familybenefits.exception.DateTimeException;
import com.example.familybenefits.exception.NotFoundException;
import com.example.familybenefits.security.service.s_interface.DBIntegrityService;
import com.example.familybenefits.service.s_interface.BenefitService;
import com.example.familybenefits.service.s_interface.DateTimeService;
import com.example.familybenefits.service.s_interface.EntityDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Реализация сервиса, управляющего объектом "пособие"
 */
@Service
public class BenefitServiceFB implements BenefitService, EntityDBService<BenefitEntity, BenefitRepository> {

  /**
   * Репозиторий, работающий с моделью таблицы "benefit"
   */
  private final BenefitRepository benefitRepository;
  /**
   * Репозиторий, работающий с моделью таблицы "user"
   */
  private final UserRepository userRepository;

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
   * Конструктор для инициализации интерфейсов репозиториев и сервисов
   * @param benefitRepository репозиторий, работающий с моделью таблицы "benefit"
   * @param userRepository репозиторий, работающий с моделью таблицы "user"
   * @param cityDBService интерфейс сервиса модели таблицы "city", целостность которой зависит от связанных таблиц
   * @param criterionDBService интерфейс сервиса модели таблицы "criterion", целостность которой зависит от связанных таблиц
   * @param dateTimeService интерфейс сервиса, который предоставляет методы для работы с датой и временем
   * @param dbIntegrityService интерфейс сервиса, отвечающего за целостность базы данных
   */
  @Autowired
  public BenefitServiceFB(BenefitRepository benefitRepository,
                          UserRepository userRepository,
                          @Lazy EntityDBService<CityEntity, CityRepository> cityDBService,
                          EntityDBService<CriterionEntity, CriterionRepository> criterionDBService,
                          DateTimeService dateTimeService,
                          DBIntegrityService dbIntegrityService) {
    this.benefitRepository = benefitRepository;
    this.userRepository = userRepository;
    this.cityDBService = cityDBService;
    this.criterionDBService = criterionDBService;
    this.dateTimeService = dateTimeService;
    this.dbIntegrityService = dbIntegrityService;
  }

  /**
   * Возвращает множество пособий, в которых есть города, учреждения и критерии.
   * Фильтр по городу, критерию и учреждению.
   * В качестве параметра может быть указан null, если данный параметр не участвует в фильтрации
   * @param idCity ID города
   * @param idCriterion ID критерия
   * @param idInstitution ID учреждения
   * @return множество кратких информаций о пособиях
   */
  @Override
  public Set<ObjectShortInfo> readAllFilter(String idCity, String idCriterion, String idInstitution) {

    return findAllFull()
        .stream()
        .filter(benefitEntity ->
                    (idCity == null || benefitEntity.getCityEntitySet()
                        .stream()
                        .map(CityEntity::getId)
                        .collect(Collectors.toSet())
                        .contains(idCity)
                    ) && (idCriterion == null || benefitEntity.getCriterionEntitySet()
                        .stream()
                        .map(CriterionEntity::getId)
                        .collect(Collectors.toSet())
                        .contains(idCriterion)
                    ) && (idInstitution == null || benefitEntity.getInstitutionEntitySet()
                        .stream()
                        .map(InstitutionEntity::getId)
                        .collect(Collectors.toSet())
                        .contains(idInstitution)))
        .map(BenefitDBConverter::toShortInfo)
        .collect(Collectors.toSet());
  }

  /**
   * Создает пособие по запросу на сохранение
   * @param benefitSave объект запроса для сохранения пособия
   * @throws AlreadyExistsException если пособие с указанным названием уже существует
   * @throws NotFoundException если город, критерий или учреждение пособия с указанным ID не найдено
   */
  @Override
  public void create(BenefitSave benefitSave) throws AlreadyExistsException, NotFoundException {

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    BenefitEntity benefitEntityFromSave = BenefitDBConverter
        .fromSave(benefitSave, dbIntegrityService::preparePostgreSQLString);

    // Проверка существования городов и критерий по их ID
    dbIntegrityService.checkExistenceById(
        cityDBService.getRepository()::existsById, benefitEntityFromSave.getCityEntitySet());
    dbIntegrityService.checkExistenceById(
        criterionDBService.getRepository()::existsById, benefitEntityFromSave.getCriterionEntitySet());

    // Проверка отсутствия пособия по его названию
    dbIntegrityService.checkAbsenceByUniqStr(
        benefitRepository::existsByName, benefitEntityFromSave.getName());

    benefitRepository.saveAndFlush(benefitEntityFromSave);
  }

  /**
   * Возвращает информацию о пособии по его ID
   * @param idBenefit ID пособия
   * @return информация о пособии
   * @throws NotFoundException если пособие с указанным ID не найден
   */
  @Override
  public BenefitInfo read(String idBenefit) throws NotFoundException {

    String prepareIdBenefit = dbIntegrityService.preparePostgreSQLString(idBenefit);

    // Получение пособия по его ID, если пособие существует
    BenefitEntity benefitEntityFromRequest = benefitRepository.findById(prepareIdBenefit)
        .orElseThrow(() -> new NotFoundException(String.format(
            "Benefit with ID \"%s\" not found", idBenefit)));

    return BenefitDBConverter.toInfo(benefitEntityFromRequest);
  }

  /**
   * Обновляет пособие по запросу на сохранение
   * @param idBenefit ID пособия
   * @param benefitSave объект запроса для сохранения пособия
   * @throws NotFoundException если пособие с указанным ID не найдено
   * @throws AlreadyExistsException если пособие с отличным ID и данным названием уже существует
   */
  @Override
  public void update(String idBenefit, BenefitSave benefitSave) throws NotFoundException, AlreadyExistsException {

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    BenefitEntity benefitEntityFromSave = BenefitDBConverter
        .fromSave(benefitSave, dbIntegrityService::preparePostgreSQLString);

    // Проверка существования городов и критерий по их ID
    dbIntegrityService.checkExistenceById(
        cityDBService.getRepository()::existsById, benefitEntityFromSave.getCityEntitySet());
    dbIntegrityService.checkExistenceById(
        criterionDBService.getRepository()::existsById, benefitEntityFromSave.getCriterionEntitySet());

    String prepareIdBenefit = dbIntegrityService.preparePostgreSQLString(idBenefit);

    // Проверка существования пособия по его ID
    dbIntegrityService.checkExistenceById(
        benefitRepository::existsById, prepareIdBenefit);

    // Проверка отсутствия пособия с отличным от данного ID и данным названием
    dbIntegrityService.checkAbsenceAnotherByUniqStr(
        benefitRepository::existsByIdIsNotAndName, prepareIdBenefit, benefitEntityFromSave.getName());

    benefitEntityFromSave.setId(prepareIdBenefit);

    benefitRepository.saveAndFlush(benefitEntityFromSave);
  }

  /**
   * Удаляет пособие по его ID
   * @param idBenefit ID пособия
   * @throws NotFoundException если пособие с указанным ID не найден
   */
  @Override
  public void delete(String idBenefit) throws NotFoundException {

    String prepareIdBenefit = dbIntegrityService.preparePostgreSQLString(idBenefit);

    // Проверка существование пособия по его ID
    dbIntegrityService.checkExistenceById(
        benefitRepository::existsById, prepareIdBenefit);

    benefitRepository.deleteById(prepareIdBenefit);
  }

  /**
   * Возвращает множество пособий, в которых нет городов, учреждений или критерий
   * @return множество кратких информаций о пособиях
   */
  @Override
  public Set<ObjectShortInfo> readAllPartial() {

    return findAllPartial()
        .stream()
        .map(BenefitDBConverter::toShortInfo)
        .collect(Collectors.toSet());
  }

  /**
   * Возвращает дополнительные данные для пособия.
   * Данные содержат в себе множества кратких информаций о городах, полных критериях и учреждениях
   * @return дополнительные данные для пособия
   */
  @Override
  public BenefitInitData getInitData() {

    return BenefitInitData
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
   * Возвращает подобранные пользователю пособия
   * @param idUser ID пользователя
   * @return множество кратких информаций о пособиях
   * @throws NotFoundException если пользователь с указанным ID не найден
   * @throws DateTimeException если критерии пользователя устарели
   */
  @Override
  public Set<ObjectShortInfo> readAllOfUser(String idUser) throws NotFoundException, DateTimeException {

    String prepareIdUser = dbIntegrityService.preparePostgreSQLString(idUser);

    // Получение пользователя по его ID, если пользователь существует
    UserEntity userEntityFromRequest = userRepository.findById(prepareIdUser)
        .orElseThrow(() -> new NotFoundException(String.format(
            "User with ID \"%s\" not found", idUser)));

    LocalDate localDateCriterion = userEntityFromRequest.getDateSelectCriterion();
    // Проверка разницы дат между текущей датой и датой последней установки критериев
    // относительно дат рождений пользователя и его детей
    dateTimeService.checkBirthdayBefore(
        userEntityFromRequest.getDateBirth(), localDateCriterion);
    dateTimeService.checkBirthdayBefore(
        userEntityFromRequest.getChildEntitySet().stream()
            .map(ChildEntity::getDateBirth).collect(Collectors.toSet()), localDateCriterion);

    // Если пособия пользователя не свежие, то
    // подбираются пособия, критерии которых включают в себя все критерии пользователя,
    // обновляется флаг свежести пособий и внесенные изменения сохраняются
    if (!userEntityFromRequest.isFreshBenefits()) {
      Set<CriterionEntity> userCriteria = userEntityFromRequest.getCriterionEntitySet();

      userEntityFromRequest.setBenefitEntitySet(
          this.findAllFull()
              .stream()
              .filter(benefitEntity -> benefitEntity.getCriterionEntitySet().containsAll(userCriteria))
              .collect(Collectors.toSet()));
      userEntityFromRequest.setFreshBenefits(true);

      userRepository.saveAndFlush(userEntityFromRequest);
    }

    return userEntityFromRequest.getBenefitEntitySet()
        .stream()
        .map(BenefitDBConverter::toShortInfo)
        .collect(Collectors.toSet());
  }

  /**
   * Возвращает репозиторий сервиса
   * @return репозиторий сервиса
   */
  @Override
  public BenefitRepository getRepository() {
    return benefitRepository;
  }

  /**
   * Возвращает множество моделей таблицы "benefit", в которых есть модели городов, учреждений и критерий
   * @return множество моделей таблиц
   */
  @Override
  public Set<BenefitEntity> findAllFull() {

    return benefitRepository
        .findAll()
        .stream()
        .filter(be -> !be.getCityEntitySet().isEmpty()
            && !be.getInstitutionEntitySet().isEmpty()
            && !be.getCriterionEntitySet().isEmpty())
        .collect(Collectors.toSet());
  }

  /**
   * Возвращает множество моделей таблицы "benefit", в которых нет моделей городов, учреждений и критерий
   * @return множество моделей таблиц
   */
  @Override
  public Set<BenefitEntity> findAllPartial() {

    return benefitRepository
        .findAll()
        .stream()
        .filter(be -> be.getCityEntitySet().isEmpty()
            || be.getInstitutionEntitySet().isEmpty()
            || be.getCriterionEntitySet().isEmpty())
        .collect(Collectors.toSet());
  }
}
