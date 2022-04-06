package com.example.familybenefits.part_res_rest_api.services.implementations;

import com.example.familybenefits.dto.entities.CriterionEntity;
import com.example.familybenefits.dto.repositories.CriterionRepository;
import com.example.familybenefits.exceptions.AlreadyExistsException;
import com.example.familybenefits.exceptions.InvalidStringException;
import com.example.familybenefits.exceptions.NotFoundException;
import com.example.familybenefits.part_res_rest_api.api_model.common.ObjectShortInfo;
import com.example.familybenefits.part_res_rest_api.api_model.criterion.CriterionInfo;
import com.example.familybenefits.part_res_rest_api.api_model.criterion.CriterionInitData;
import com.example.familybenefits.part_res_rest_api.api_model.criterion.CriterionSave;
import com.example.familybenefits.part_res_rest_api.converters.CriterionDBConverter;
import com.example.familybenefits.part_res_rest_api.services.interfaces.CriterionService;
import com.example.familybenefits.part_res_rest_api.services.interfaces.CriterionTypeService;
import com.example.familybenefits.part_res_rest_api.services.interfaces.UserService;
import com.example.familybenefits.security.DBSecuritySupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация сервиса, управляющего объектом "критерий"
 */
@Service
public class CriterionServiceFB implements CriterionService {

  /**
   * Репозиторий, работающий с моделью таблицы "criterion"
   */
  private final CriterionRepository criterionRepository;

  /**
   * Интерфейс сервиса, управляющего объектом "тип критерия"
   */
  private final CriterionTypeService criterionTypeService;
  /**
   * Интерфейс сервиса, управляющего объектом "пользователь"
   */
  private final UserService userService;

  /**
   * Конструктор для инициализации интерфейсов репозиториев и сервиса
   * @param criterionRepository репозиторий, работающий с моделью таблицы "criterion"
   * @param criterionTypeService интерфейс сервиса, управляющего объектом "тип критерия"
   * @param userService интерфейс сервиса, управляющего объектом "пользователь"
   */
  @Autowired
  public CriterionServiceFB(CriterionRepository criterionRepository,
                            CriterionTypeService criterionTypeService,
                            UserService userService) {
    this.criterionRepository = criterionRepository;
    this.criterionTypeService = criterionTypeService;
    this.userService = userService;
  }

  /**
   * Возвращает список критерий, в которых есть пособия.
   * Фильтр по названию, ID пособия или типа критерия.
   * В качестве параметра может быть указан null, если данный параметр не участвует в фильтрации
   * @param name название критерия
   * @param idBenefit ID пособия
   * @param idCriterionType ID типа критерия
   * @return список кратких информаций о критериях
   */
  @Override
  public List<ObjectShortInfo> readAllFilter(String name, String idBenefit, String idCriterionType) {

    String preparedName = DBSecuritySupport.preparePostgreSQLString(name);
    String preparedIdBenefit = DBSecuritySupport.preparePostgreSQLString(idBenefit);
    String preparedIdCriterionType = DBSecuritySupport.preparePostgreSQLString(idCriterionType);

    return criterionRepository.findAllFilter(preparedName, preparedIdBenefit, preparedIdCriterionType)
        .stream()
        .map(CriterionDBConverter::toShortInfo)
        .collect(Collectors.toList());
  }

  /**
   * Создает критерий по запросу на сохранение
   * @param criterionSave объект запроса для сохранения критерия
   * @throws AlreadyExistsException если критерий с указанным названием уже существует
   * @throws NotFoundException если тип критерия данного критерия с указанным ID не найден
   * @throws InvalidStringException если строковое поле объекта запроса не содержит букв или цифр
   */
  @Override
  public void create(CriterionSave criterionSave) throws AlreadyExistsException, NotFoundException, InvalidStringException {

    // Проверка отсутствия критерия по его названию
    DBSecuritySupport.checkAbsenceByUniqStr(criterionRepository::existsByName, criterionSave.getName());
    // Проверка существования типа критерия по его ID
    DBSecuritySupport.checkExistenceById(criterionTypeService::existsById, criterionSave.getIdCriterionType());

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    CriterionEntity criterionEntityFromSave = CriterionDBConverter
        .fromSave(null, criterionSave, DBSecuritySupport::preparePostgreSQLString);

    criterionRepository.saveAndFlush(criterionEntityFromSave);
  }

  /**
   * Возвращает информацию о критерии по его ID
   * @param idCriterion ID критерия
   * @return информация о критерии
   * @throws NotFoundException если критерий с указанным ID не найден
   */
  @Override
  public CriterionInfo read(String idCriterion) throws NotFoundException {

    // Получение критерия по его ID, если критерий существует
    String prepareIdCriterion = DBSecuritySupport.preparePostgreSQLString(idCriterion);
    CriterionEntity criterionEntityFromRequest = criterionRepository.findById(prepareIdCriterion).orElseThrow(
        () -> new NotFoundException(String.format("Criterion with ID \"%s\" not found", idCriterion)));

    return CriterionDBConverter
        .toInfo(criterionEntityFromRequest, criterionTypeService.readNameByIdCriterion(idCriterion));
  }

  /**
   * Обновляет данные критерия по запросу на сохранение
   * @param idCriterion ID критерия
   * @param criterionSave объект запроса для сохранения критерия
   * @throws NotFoundException если критерий с указанными данными не найден
   * @throws AlreadyExistsException если критерий с отличным ID и данным названием уже существует
   * @throws InvalidStringException если строковое поле объекта запроса не содержит букв или цифр
   */
  @Override
  public void update(String idCriterion, CriterionSave criterionSave) throws NotFoundException, AlreadyExistsException, InvalidStringException {

    // Проверка существования критерия по его ID
    DBSecuritySupport.checkExistenceById(criterionRepository::existsById, idCriterion);

    // Проверка существования типа критерия по его ID
    DBSecuritySupport.checkExistenceById(criterionTypeService::existsById, idCriterion);

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    CriterionEntity criterionEntityFromSave = CriterionDBConverter
        .fromSave(idCriterion, criterionSave, DBSecuritySupport::preparePostgreSQLString);

    // Проверка отсутствия критерия с отличным от данного ID и данным названием
    DBSecuritySupport.checkAbsenceAnotherByUniqStr(
        criterionRepository::existsByIdIsNotAndName, idCriterion, criterionSave.getName());

    criterionEntityFromSave.setId(idCriterion);

    criterionRepository.saveAndFlush(criterionEntityFromSave);
  }

  /**
   * Удаляет критерий по его ID
   * @param idCriterion ID критерия
   * @throws NotFoundException если критерий с указанным ID не найден
   */
  @Override
  public void delete(String idCriterion) throws NotFoundException {

    // Проверка существования критерия по его ID
    DBSecuritySupport.checkExistenceById(criterionRepository::existsById, idCriterion);

    criterionRepository.deleteById(idCriterion);
  }

  /**
   * Возвращает список критерий, в которых нет пособий
   * @return список кратких информаций о критериях
   */
  @Override
  public List<ObjectShortInfo> readAllPartial() {

    return criterionRepository.findAllPartial()
        .stream()
        .map(CriterionDBConverter::toShortInfo)
        .collect(Collectors.toList());
  }

  /**
   * Возвращает дополнительные данные для критерия.
   * Данные содержат в себе список кратких информаций о типах критерий
   * @return дополнительные данные для критерия
   */
  @Override
  public CriterionInitData getInitData() {

    return CriterionInitData
        .builder()
        .shortCriterionTypeList(criterionTypeService.readAllFullShort())
        .build();
  }

  /**
   * Возвращает критерии пользователя
   * @param idUser ID пользователя
   * @return список кратких информаций о критериях
   * @throws NotFoundException если пользователь не найден
   */
  @Override
  public List<ObjectShortInfo> readAllOfUser(String idUser) throws NotFoundException {

    // Проверка существования пользователя по его ID
    DBSecuritySupport.checkExistenceById(userService::existsById, idUser);

    return criterionRepository.findAllByIdUser(idUser)
        .stream()
        .map(CriterionDBConverter::toShortInfo)
        .collect(Collectors.toList());
  }

  /**
   * Возвращает критерии пособия
   * @param idBenefit ID пособия
   * @return список кратких информаций о критериях
   */
  @Override
  public List<ObjectShortInfo> readAllOfBenefit(String idBenefit) {

    String preparedIdBenefit = DBSecuritySupport.preparePostgreSQLString(idBenefit);
    return criterionRepository.findAllFilter(null, preparedIdBenefit, null)
        .stream()
        .map(CriterionDBConverter::toShortInfo)
        .collect(Collectors.toList());
  }

  /**
   * Проверяет существование критерия по его ID
   * @param idCity ID критерия, предварительно обработанный
   * @return true, если критерий найден
   */
  @Override
  public boolean existsById(String idCity) {

    return criterionRepository.existsById(idCity);
  }

  /**
   * Возвращает список информаций о критериях, в которых есть пособия
   * @return список информаций о критериях
   */
  @Override
  public List<CriterionInfo> readAllFull() {

    return criterionRepository.findAllFilter(null, null, null)
        .stream()
        .map(criterionEntity -> CriterionDBConverter
            .toInfo(criterionEntity, criterionTypeService.readNameByIdCriterion(criterionEntity.getId())))
        .collect(Collectors.toList());
  }
}
