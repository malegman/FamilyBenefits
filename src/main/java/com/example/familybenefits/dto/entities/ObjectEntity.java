package com.example.familybenefits.dto.entities;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Базовый класс для моделей таблиц
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public abstract class ObjectEntity {

  /**
   * ID объекта
   */
  private String id;
}
