package com.example.familybenefits.part_res_rest_api.services.implementations;

import com.example.familybenefits.dto.entities.InstitutionEntity;
import com.example.familybenefits.dto.repositories.InstitutionRepository;
import com.example.familybenefits.exceptions.AlreadyExistsException;
import com.example.familybenefits.exceptions.InvalidStringException;
import com.example.familybenefits.exceptions.NotFoundException;
import com.example.familybenefits.part_res_rest_api.api_model.common.ObjectShortInfo;
import com.example.familybenefits.part_res_rest_api.api_model.institution.InstitutionInfo;
import com.example.familybenefits.part_res_rest_api.api_model.institution.InstitutionInitData;
import com.example.familybenefits.part_res_rest_api.api_model.institution.InstitutionSave;
import com.example.familybenefits.part_res_rest_api.converters.InstitutionDBConverter;
import com.example.familybenefits.part_res_rest_api.services.interfaces.BenefitService;
import com.example.familybenefits.part_res_rest_api.services.interfaces.CityService;
import com.example.familybenefits.part_res_rest_api.services.interfaces.InstitutionService;
import com.example.familybenefits.security.DBSecuritySupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация сервиса, управляющего объектом "учреждение"
 */
@Service
public class InstitutionServiceFB implements InstitutionService {

  /**
   * Репозиторий, работающий с моделью таблицы "institution"
   */
  private final InstitutionRepository institutionRepository;

  /**
   * Интерфейс сервиса, управляющего объектом "город"
   */
  private final CityService cityService;
  /**
   * Интерфейс сервиса, управляющего объектом "пособие"
   */
  private final BenefitService benefitService;

  /**
   * Конструктор для инициализации интерфейсов репозиториев и сервиса
   * @param institutionRepository репозиторий, работающий с моделью таблицы "institution"
   * @param cityService интерфейс сервиса, управляющего объектом "город"
   * @param benefitService интерфейс сервиса, управляющего объектом "пособие"
   */
  @Autowired
  public InstitutionServiceFB(InstitutionRepository institutionRepository,
                              CityService cityService,
                              BenefitService benefitService) {
    this.institutionRepository = institutionRepository;
    this.cityService = cityService;
    this.benefitService = benefitService;
  }

  /**
   * Возвращает список учреждений, в которых есть пособия и города.
   * Фильтр по названию, ID города или пособия.
   * В качестве параметра может быть указан null, если данный параметр не участвует в фильтрации
   * @param name название учреждения
   * @param idCity ID города
   * @param idBenefit ID пособия
   * @return список кратких информаций об учреждениях
   */
  @Override
  public List<ObjectShortInfo> readAllFilter(String name, String idCity, String idBenefit) {

    String preparedName = DBSecuritySupport.preparePostgreSQLString(name);
    String preparedIdCity = DBSecuritySupport.preparePostgreSQLString(idCity);
    String preparedIdBenefit = DBSecuritySupport.preparePostgreSQLString(idBenefit);

    return institutionRepository.findAllFilter(preparedName, preparedIdCity, preparedIdBenefit)
        .stream()
        .map(InstitutionDBConverter::toShortInfo)
        .collect(Collectors.toList());
  }

  /**
   * Создает учреждение по запросу на сохранение
   * @param institutionSave объект запроса на сохранение учреждения
   * @throws AlreadyExistsException если учреждение с таким названием уже существует
   * @throws NotFoundException если город или пособия с указанными ID не найдены
   * @throws InvalidStringException если строковое поле объекта запроса не содержит букв или цифр
   */
  @Override
  public void create(InstitutionSave institutionSave) throws AlreadyExistsException, NotFoundException, InvalidStringException {

    // Проверка отсутствия учреждения по его названию
    DBSecuritySupport.checkAbsenceByUniqStr(institutionRepository::existsByName, institutionSave.getName());

    // Проверка существования города и пособий по их ID
    DBSecuritySupport.checkExistenceById(cityService::existsById, institutionSave.getIdCity());
    DBSecuritySupport.checkExistenceById(benefitService::existsById, institutionSave.getIdBenefitList());

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    InstitutionEntity institutionEntityFromSave = InstitutionDBConverter
        .fromSave(null, institutionSave, DBSecuritySupport::preparePostgreSQLString);

    institutionRepository.saveAndFlush(institutionEntityFromSave);
  }

  /**
   * Возвращает информацию об учреждении по его ID
   * @param idInstitution ID учреждения
   * @return информация об учреждении
   * @throws NotFoundException если учреждение с указанным ID не найдено
   */
  @Override
  public InstitutionInfo read(String idInstitution) throws NotFoundException {

    // Получение учреждения по его ID, если учреждение существует
    String prepareIdInstitution = DBSecuritySupport.preparePostgreSQLString(idInstitution);
    InstitutionEntity institutionEntityFromRequest = institutionRepository.findById(prepareIdInstitution).orElseThrow(
        () -> new NotFoundException(String.format("Institution with ID \"%s\" not found", idInstitution)));

    return InstitutionDBConverter
        .toInfo(institutionEntityFromRequest, cityService.readName(institutionEntityFromRequest.getIdCity()));
  }

  /**
   * Обновляет учреждение по запросу на сохранение
   * @param idInstitution ID учреждения
   * @param institutionSave объект запроса на сохранение учреждения
   * @throws NotFoundException если учреждение, город или пособия с указанными ID не найдены
   * @throws AlreadyExistsException если учреждение с отличным ID и данным названием уже существует
   * @throws InvalidStringException если строковое поле объекта запроса не содержит букв или цифр
   */
  @Override
  public void update(String idInstitution, InstitutionSave institutionSave) throws NotFoundException, AlreadyExistsException, InvalidStringException {

    // Проверка существования учреждения по его ID
    DBSecuritySupport.checkExistenceById(institutionRepository::existsById, idInstitution);

    // Проверка отсутствия учреждения с отличным от данного ID и данным названием
    DBSecuritySupport.checkAbsenceAnotherByUniqStr(
        institutionRepository::existsByIdIsNotAndName, idInstitution, institutionSave.getName());

    // Проверка существования города и пособий по их ID
    DBSecuritySupport.checkExistenceById(cityService::existsById, institutionSave.getIdCity());
    DBSecuritySupport.checkExistenceById(benefitService::existsById, institutionSave.getIdBenefitList());

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    InstitutionEntity institutionEntityFromSave = InstitutionDBConverter
        .fromSave(idInstitution, institutionSave, DBSecuritySupport::preparePostgreSQLString);

    institutionEntityFromSave.setId(idInstitution);

    institutionRepository.saveAndFlush(institutionEntityFromSave);
  }

  /**
   * Удаляет учреждение по его ID
   * @param idInstitution ID учреждения
   * @throws NotFoundException если учреждение с указанными данными не найдено
   */
  @Override
  public void delete(String idInstitution) throws NotFoundException {

    // Проверка существования учреждения по его ID
    DBSecuritySupport.checkExistenceById(institutionRepository::existsById, idInstitution);

    institutionRepository.deleteById(idInstitution);
  }

  /**
   * Возвращает список учреждений, в которых нет пособий
   * @return список кратких информаций об учреждениях
   */
  @Override
  public List<ObjectShortInfo> readAllPartial() {

    return institutionRepository.findAllPartial()
        .stream()
        .map(InstitutionDBConverter::toShortInfo)
        .collect(Collectors.toList());
  }

  /**
   * Возвращает дополнительные данные для учреждения.
   * Данные содержат в себе список кратких информаций о городах.
   * @return дополнительные данные для учреждения
   */
  @Override
  public InstitutionInitData getInitData() {

    return InstitutionInitData
        .builder()
        .shortCityList(cityService.readAllFullShort())
        .shortBenefitList(benefitService.findAllFullShort())
        .build();
  }

  /**
   * Проверяет существование учреждения по его ID
   * @param idInstitution ID учреждения, предварительно обработанный
   * @return true, если учреждение найдено
   */
  @Override
  public boolean existsById(String idInstitution) {

    return institutionRepository.existsById(idInstitution);
  }

  /**
   * Возвращает список кратких информаций учреждений, в которых есть пособие и город
   * @return список кратких информаций учреждений
   */
  @Override
  public List<ObjectShortInfo> findAllFullShort() {

    return institutionRepository.findAllFilter(null, null, null)
        .stream()
        .map(InstitutionDBConverter::toShortInfo)
        .collect(Collectors.toList());
  }
}
