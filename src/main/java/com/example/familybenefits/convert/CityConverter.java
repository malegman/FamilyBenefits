package com.example.familybenefits.convert;

import com.example.familybenefits.api_model.city.CityAdd;
import com.example.familybenefits.api_model.city.CityInfo;
import com.example.familybenefits.api_model.city.CityUpdate;
import com.example.familybenefits.api_model.common.ShortObjectInfo;
import com.example.familybenefits.dao.entity.CityEntity;

/**
 * Класс преобразования модели таблицы "city" в другие объекты и получения из других объектов
 */
public class CityConverter {

  /**
   * Преобразование объекта запроса на добавление города в модель таблицы "city"
   * @param cityAdd объект запроса на добавление города
   * @return модель таблицы "city"
   */
  static public CityEntity fromAdd(CityAdd cityAdd) {

    return CityEntity
        .builder()
        .name(cityAdd.getName())
        .info(cityAdd.getInfo())
        .build();
  }

  /**
   * Преобразование объекта запроса на обновление города в модель таблицы "city"
   * @param cityUpdate объект запроса на обновление города
   * @return модель таблицы "city"
   */
  static public CityEntity fromUpdate(CityUpdate cityUpdate) {

    return CityEntity
        .builder()
        .id(cityUpdate.getId())
        .name(cityUpdate.getName())
        .build();
  }

  /**
   * Преобразование объекта краткой информации о городе в модель таблицы "city"
   * @param shortObjectInfo краткая информация о городе
   * @return модель таблицы "city"
   */
  static public CityEntity fromShortInfo(ShortObjectInfo shortObjectInfo) {

    return CityEntity
        .builder()
        .id(shortObjectInfo.getIdObject())
        .name(shortObjectInfo.getNameObject())
        .build();
  }

  /**
   * Преобразование модели таблицы "city" в объект информации о городе
   * @param cityEntity модель таблицы "city"
   * @return информация о городе
   */
  static public CityInfo toInfo(CityEntity cityEntity) {

    return CityInfo
        .builder()
        .id(cityEntity.getId())
        .name(cityEntity.getName())
        .info(cityEntity.getInfo())
        .build();
  }

  /**
   * Преобразование модели таблицы "city" в объект краткой информации об объекте
   * @param cityEntity модель таблицы "city"
   * @return краткая информция о городе
   */
  static public ShortObjectInfo toShortInfo(CityEntity cityEntity) {

    return ShortObjectInfo
        .builder()
        .idObject(cityEntity.getId())
        .nameObject(cityEntity.getName())
        .build();
  }
}
