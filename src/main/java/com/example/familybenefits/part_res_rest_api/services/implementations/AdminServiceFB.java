package com.example.familybenefits.part_res_rest_api.services.implementations;

import com.example.familybenefits.dto.entities.UserEntity;
import com.example.familybenefits.dto.repositories.RoleRepository;
import com.example.familybenefits.dto.repositories.UserRepository;
import com.example.familybenefits.exceptions.AlreadyExistsException;
import com.example.familybenefits.exceptions.InvalidEmailException;
import com.example.familybenefits.exceptions.InvalidStringException;
import com.example.familybenefits.exceptions.NotFoundException;
import com.example.familybenefits.part_res_rest_api.api_model.admin.AdminInfo;
import com.example.familybenefits.part_res_rest_api.api_model.admin.AdminSave;
import com.example.familybenefits.part_res_rest_api.converters.AdminDBConverter;
import com.example.familybenefits.part_res_rest_api.services.interfaces.AdminService;
import com.example.familybenefits.security.DBSecuritySupport;
import com.example.familybenefits.security.MailSecuritySupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Реализация сервиса, управляющего объектом "администратор"
 */
@Slf4j
@Service
public class AdminServiceFB implements AdminService {

  /**
   * Репозиторий, работающий с моделью таблицы "user"
   */
  private final UserRepository userRepository;

  /**
   * Репозиторий, работающий с моделью таблицы "role"
   */
  private final RoleRepository roleRepository;

  /**
   * Конструктор для инициализации интерфейсов репозиториев и сервисов
   * @param userRepository репозиторий, работающий с моделью таблицы "user"
   * @param roleRepository репозиторий, работающий с моделью таблицы "role"
   */
  @Autowired
  public AdminServiceFB(UserRepository userRepository,
                        RoleRepository roleRepository) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
  }

  /**
   * Возвращает администратора об учреждении по его ID
   * @param idAdmin ID администратора
   * @return информация об администраторе
   * @throws NotFoundException если администратор с данным ID не найден
   */
  @Override
  public AdminInfo read(String idAdmin) throws NotFoundException {

    // Получение администратора по его ID, если администратор существует
    String preparedIdAdmin = DBSecuritySupport.preparePostgreSQLString(idAdmin);
    UserEntity userEntityFromRequest = userRepository.findById(preparedIdAdmin).orElseThrow(
        () -> new NotFoundException(String.format("Administrator with ID \"%s\" not found", idAdmin)));

    return AdminDBConverter.toInfo(userEntityFromRequest, roleRepository.findAllByIdUser(preparedIdAdmin));
  }

  /**
   * Обновляет администратора по запросу на сохранение
   * @param idAdmin ID администратора
   * @param adminSave объект запроса на сохранение администратора
   * @throws NotFoundException если администратор с указанными данными не найден
   * @throws InvalidEmailException если указанный "email" не является email
   * @throws AlreadyExistsException если администратор или пользователь с отличным ID и данным email уже существует
   * @throws InvalidStringException если строковое поле объекта запроса не содержит букв или цифр
   */
  @Override
  public void update(String idAdmin, AdminSave adminSave) throws NotFoundException, InvalidEmailException, AlreadyExistsException, InvalidStringException {

    // Проверка строки email на соответствие формату email
    MailSecuritySupport.checkEmailElseThrow(adminSave.getEmail());

    // Проверка отсутствия пользователя с отличным от данного ID и данным email
    DBSecuritySupport.checkAbsenceAnotherByUniqStr(
        userRepository::existsByIdIsNotAndEmail, idAdmin, adminSave.getEmail());

    // Получение администратора по его ID, если администратора существует
    String preparedIdAdmin = DBSecuritySupport.preparePostgreSQLString(idAdmin);
    UserEntity userEntityFromDB = userRepository.findById(preparedIdAdmin).orElseThrow(
        () -> new NotFoundException(String.format("Administrator with ID \"%s\" not found", idAdmin)));

    // Получение модели таблицы из запроса с подготовкой строковых значений для БД
    UserEntity userEntityFromSave = AdminDBConverter
        .fromSave(idAdmin, adminSave, DBSecuritySupport::preparePostgreSQLString);

    userEntityFromDB.setEmail(userEntityFromSave.getEmail());
    userEntityFromDB.setName(userEntityFromSave.getName());

    userRepository.save(userEntityFromDB);
    log.info("DB. Administrator with ID \"{}\" updated.", idAdmin);
  }
}
