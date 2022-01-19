package com.example.familybenefits.convert;

import com.example.familybenefits.api_model.city.CityAdd;
import com.example.familybenefits.api_model.city.CityInfo;
import com.example.familybenefits.api_model.city.CityUpdate;
import com.example.familybenefits.api_model.common.ObjectShortInfo;
import com.example.familybenefits.dao.entity.CityEntity;

/**
 * Класс преобразования модели таблицы "city" в другие объекты и получения из других объектов
 */
public class CityConverter {

  /**
   * Преобразует объект запроса на добавление города в модель таблицы "city"
   * @param cityAdd объект запроса на добавление города
   * @return модель таблицы "city"
   */
  static public CityEntity fromAdd(CityAdd cityAdd) {

    if (cityAdd == null) {
      return new CityEntity();
    }

    return CityEntity
        .builder()
        .name(cityAdd.getName())
        .info(cityAdd.getInfo())
        .build();
  }

  /**
   * Преобразует объект запроса на обновление города в модель таблицы "city"
   * @param cityUpdate объект запроса на обновление города
   * @return модель таблицы "city"
   */
  static public CityEntity fromUpdate(CityUpdate cityUpdate) {

    if (cityUpdate == null) {
      return new CityEntity();
    }

    return CityEntity
        .builder()
        .id(cityUpdate.getId())
        .name(cityUpdate.getName())
        .info(cityUpdate.getInfo())
        .build();
  }

  /**
   * Преобразует модель таблицы "city" в объект информации о городе
   * @param cityEntity модель таблицы "city"
   * @return информация о городе
   */
  static public CityInfo toInfo(CityEntity cityEntity) {

    if (cityEntity == null) {
      return new CityInfo();
    }

    return CityInfo
        .builder()
        .id(cityEntity.getId())
        .name(cityEntity.getName())
        .info(cityEntity.getInfo())
        .build();
  }

  /**
   * Преобразует модель таблицы "city" в объект краткой информации об объекте
   * @param cityEntity модель таблицы "city"
   * @return краткая информция о городе
   */
  static public ObjectShortInfo toShortInfo(CityEntity cityEntity) {

    if (cityEntity == null) {
      return new ObjectShortInfo();
    }

    return ObjectShortInfo
        .builder()
        .idObject(cityEntity.getId())
        .nameObject(cityEntity.getName())
        .build();
  }
}
