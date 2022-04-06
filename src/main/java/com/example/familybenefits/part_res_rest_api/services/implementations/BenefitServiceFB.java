package com.example.familybenefits.part_res_rest_api.services.implementations;

import com.example.familybenefits.dto.repositories.ChildBirthRepository;
import com.example.familybenefits.exceptions.InvalidStringException;
import com.example.familybenefits.part_res_rest_api.api_model.benefit.BenefitInfo;
import com.example.familybenefits.part_res_rest_api.api_model.benefit.BenefitInitData;
import com.example.familybenefits.part_res_rest_api.api_model.benefit.BenefitSave;
import com.example.familybenefits.part_res_rest_api.api_model.common.ObjectShortInfo;
import com.example.familybenefits.part_res_rest_api.converters.BenefitDBConverter;
import com.example.familybenefits.dto.entities.*;
import com.example.familybenefits.dto.repositories.BenefitRepository;
import com.example.familybenefits.dto.repositories.UserRepository;
import com.example.familybenefits.exceptions.AlreadyExistsException;
import com.example.familybenefits.exceptions.DateTimeException;
import com.example.familybenefits.exceptions.NotFoundException;
import com.example.familybenefits.part_res_rest_api.services.interfaces.BenefitService;
import com.example.familybenefits.part_res_rest_api.services.interfaces.CityService;
import com.example.familybenefits.part_res_rest_api.services.interfaces.CriterionService;
import com.example.familybenefits.part_res_rest_api.services.interfaces.InstitutionService;
import com.example.familybenefits.security.DBSecuritySupport;
import com.example.familybenefits.security.DateTimeSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация сервиса, управляющего объектом "пособие"
 */
@Service
public class BenefitServiceFB implements BenefitService {

  /**
   * Репозиторий, работающий с моделью таблицы "benefit"
   */
  private final BenefitRepository benefitRepository;
  /**
   * Репозиторий, работающий с моделью таблицы "user"
   */
  private final UserRepository userRepository;
  /**
   * Репозиторий, работающий с моделью таблицы "child"
   */
  private final ChildBirthRepository childBirthRepository;

  /**
   * Интерфейс сервиса, управляющего объектом "город"
   */
  private final CityService cityService;
  /**
   * Интерфейс сервиса, управляющего объектом "критерий"
   */
  private final CriterionService criterionService;
  /**
   * Интерфейс сервиса, управляющего объектом "учреждение"
   */
  private final InstitutionService institutionService;

  /**
   * Конструктор для инициализации интерфейсов репозиториев и сервисов
   * @param benefitRepository репозиторий, работающий с моделью таблицы "benefit"
   * @param userRepository репозиторий, работающий с моделью таблицы "user"
   * @param childBirthRepository репозиторий, работающий с моделью таблицы "child"
   * @param cityService интерфейс сервиса, управляющего объектом "город"
   * @param criterionService интерфейс сервиса, управляющего объектом "критерий"
   * @param institutionService интерфейс сервиса, управляющего объектом "учреждение"
   */
  @Autowired
  public BenefitServiceFB(BenefitRepository benefitRepository,
                          UserRepository userRepository,
                          ChildBirthRepository childBirthRepository,
                          @Lazy CityService cityService,
                          CriterionService criterionService,
                          InstitutionService institutionService) {
    this.benefitRepository = benefitRepository;
    this.userRepository = userRepository;
    this.childBirthRepository = childBirthRepository;
    this.cityService = cityService;
    this.criterionService = criterionService;
    this.institutionService = institutionService;
  }

  /**
   * Возвращает список пособий, в которых есть города, учреждения и критерии.
   * Фильтр по названию, ID города, критерия и учреждения.
   * В качестве параметра может быть указан null, если данный параметр не участвует в фильтрации
   * @param name название пособия
   * @param idCity ID города
   * @param idCriterion ID критерия
   * @param idInstitution ID учреждения
   * @return список кратких информаций о пособиях
   */
  @Override
  public List<ObjectShortInfo> readAllFilter(String name, String idCity, String idCriterion, String idInstitution) {

    String preparedName = DBSecuritySupport.preparePostgreSQLString(name);
    String preparedIdCity = DBSecuritySupport.preparePostgreSQLString(idCity);
    String preparedIdCriterion = DBSecuritySupport.preparePostgreSQLString(idCriterion);
    String preparedIdInstitution = DBSecuritySupport.preparePostgreSQLString(idInstitution);

    return benefitRepository.findAllFilter(
        preparedName, preparedIdCity, preparedIdCriterion, preparedIdInstitution)
        .stream()
        .map(BenefitDBConverter::toShortInfo)
        .collect(Collectors.toList());
  }

  /**
   * Создает пособие по запросу на сохранение
   * @param benefitSave объект запроса для сохранения пособия
   * @throws AlreadyExistsException если пособие с указанным названием уже существует
   * @throws NotFoundException если город, критерий или учреждение пособия с указанным ID не найдено
   * @throws InvalidStringException если строковое поле объекта запроса не содержит букв или цифр
   */
  @Override
  public void create(BenefitSave benefitSave) throws AlreadyExistsException, NotFoundException, InvalidStringException {

    // Проверка существования городов, критерий и учреждений по их ID
    DBSecuritySupport.checkExistenceById(cityService::existsById, benefitSave.getIdCityList());
    DBSecuritySupport.checkExistenceById(criterionService::existsById, benefitSave.getIdCriterionList());
    DBSecuritySupport.checkExistenceById(institutionService::existsById, benefitSave.getIdInstitutionList());

    // Проверка отсутствия пособия по его названию
    DBSecuritySupport.checkAbsenceByUniqStr(benefitRepository::existsByName, benefitSave.getName());

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    BenefitEntity benefitEntityFromSave = BenefitDBConverter
        .fromSave(null, benefitSave, DBSecuritySupport::preparePostgreSQLString);

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

    // Получение пособия по его ID, если пособие существует
    String prepareIdBenefit = DBSecuritySupport.preparePostgreSQLString(idBenefit);
    BenefitEntity benefitEntityFromRequest = benefitRepository.findById(prepareIdBenefit).orElseThrow(
        () -> new NotFoundException(String.format("Benefit with ID \"%s\" not found", idBenefit)));

    return BenefitDBConverter.toInfo(benefitEntityFromRequest);
  }

  /**
   * Обновляет пособие по запросу на сохранение
   * @param idBenefit ID пособия
   * @param benefitSave объект запроса для сохранения пособия
   * @throws NotFoundException если пособие с указанным ID не найдено
   * @throws AlreadyExistsException если пособие с отличным ID и данным названием уже существует
   * @throws InvalidStringException если строковое поле объекта запроса не содержит букв или цифр
   */
  @Override
  public void update(String idBenefit, BenefitSave benefitSave) throws NotFoundException, AlreadyExistsException, InvalidStringException {

    // Проверка существования пособия по его ID
    DBSecuritySupport.checkExistenceById(benefitRepository::existsById, idBenefit);

    // Проверка отсутствия пособия с отличным от данного ID и данным названием
    DBSecuritySupport.checkAbsenceAnotherByUniqStr(
        benefitRepository::existsByIdIsNotAndName, idBenefit, benefitSave.getName());

    // Проверка существования городов, критерий и учреждений по их ID
    DBSecuritySupport.checkExistenceById(cityService::existsById, benefitSave.getIdCityList());
    DBSecuritySupport.checkExistenceById(criterionService::existsById, benefitSave.getIdCriterionList());
    DBSecuritySupport.checkExistenceById(institutionService::existsById, benefitSave.getIdInstitutionList());

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    BenefitEntity benefitEntityFromSave = BenefitDBConverter
        .fromSave(idBenefit, benefitSave, DBSecuritySupport::preparePostgreSQLString);

    benefitEntityFromSave.setId(idBenefit);

    benefitRepository.saveAndFlush(benefitEntityFromSave);
  }

  /**
   * Удаляет пособие по его ID
   * @param idBenefit ID пособия
   * @throws NotFoundException если пособие с указанным ID не найден
   */
  @Override
  public void delete(String idBenefit) throws NotFoundException {

    // Проверка существование пособия по его ID
    DBSecuritySupport.checkExistenceById(benefitRepository::existsById, idBenefit);

    benefitRepository.deleteById(idBenefit);
  }

  /**
   * Возвращает список пособий, в которых нет городов, учреждений или критерий
   * @return список кратких информаций о пособиях
   */
  @Override
  public List<ObjectShortInfo> readAllPartial() {

    return benefitRepository.findAllPartial()
        .stream()
        .map(BenefitDBConverter::toShortInfo)
        .collect(Collectors.toList());
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
        .shortCityList(cityService.readAllFullShort())
        .criterionList(criterionService.readAllFull())
        .shortInstitutionList(institutionService.findAllFullShort())
        .build();
  }

  /**
   * Возвращает подобранные пользователю пособия
   * @param idUser ID пользователя
   * @return список кратких информаций о пособиях
   * @throws NotFoundException если пользователь с указанным ID не найден
   * @throws DateTimeException если критерии пользователя устарели
   */
  @Override
  public List<ObjectShortInfo> readAllOfUser(String idUser) throws NotFoundException, DateTimeException {

    // Получение пользователя по его ID, если пользователь существует
    String preparedIdUser = DBSecuritySupport.preparePostgreSQLString(idUser);
    UserEntity userEntityFromRequest = userRepository.findById(preparedIdUser).orElseThrow(
        () -> new NotFoundException(String.format("User with ID \"%s\" not found", idUser)));

    LocalDate localDateCriterion = userEntityFromRequest.getDateSelectCriterion();
    // Проверка разницы дат между текущей датой и датой последней установки критериев
    // относительно дат рождений пользователя и его детей
    DateTimeSupport.checkBirthdayBefore(userEntityFromRequest.getDateBirth(), localDateCriterion);
    DateTimeSupport.checkChildBirthdayBefore(childBirthRepository.findAllByIdUser(idUser), localDateCriterion);

    // Если пособия пользователя не свежие, то
    // подбираются пособия, критерии которых включают в себя все критерии пользователя,
    // обновляется флаг свежести пособий и внесенные изменения сохраняются
    List<BenefitEntity> usersBenefitList;

    if (!userEntityFromRequest.isFreshBenefits()) {
      userRepository.deleteAllBenefits(idUser);

      List<ObjectShortInfo> userCriteria = criterionService.readAllOfUser(idUser);
      usersBenefitList = benefitRepository.findAllFilter(null, userEntityFromRequest.getIdCity(), null, null);
      usersBenefitList
          .stream()
          .filter(benefitEntity -> userCriteria.containsAll(criterionService.readAllFilter(null, benefitEntity.getId(), null)))
          .forEach(benefitEntity -> userRepository.addBenefit(idUser, benefitEntity.getId()));

      userEntityFromRequest.setFreshBenefits(true);

    } else {
      usersBenefitList = benefitRepository.findAllByIdUser(idUser);
    }

    return usersBenefitList
        .stream()
        .map(BenefitDBConverter::toShortInfo)
        .collect(Collectors.toList());
  }

  /**
   * Проверяет существование пособия по его ID
   * @param idBenefit ID пособия, предварительно обработанный
   * @return true, если пособие найдено
   */
  @Override
  public boolean existsById(String idBenefit) {

    return benefitRepository.existsById(idBenefit);
  }

  /**
   * Возвращает список кратких информаций пособий, в которых есть город, критерий и учреждение
   * @return список кратких информаций пособий
   */
  @Override
  public List<ObjectShortInfo> findAllFullShort() {

    return benefitRepository.findAllFilter(null, null, null, null)
        .stream()
        .map(BenefitDBConverter::toShortInfo)
        .collect(Collectors.toList());
  }
}
