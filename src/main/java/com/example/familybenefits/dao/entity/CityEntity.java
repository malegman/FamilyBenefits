package com.example.familybenefits.dao.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

/**
 * Модель записи таблицы "cityEntity"
 */
@Entity
@Table(name = "city", schema = "family_benefit")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class CityEntity implements DBPreparer {

  /**
   * ID города
   */
  @NonNull
  @Id
  @Column(name = "id")
  private BigInteger id;

  /**
   * Название города
   */
  @NonNull
  @Column(name = "name")
  private String name;

  /**
   * Информация города
   */
  @Nullable
  @Column(name = "info")
  private String info;

  /**
   * Множество учреждений города
   */
  @NonNull
  @ToString.Exclude
  @OneToMany(mappedBy = "cityEntity")
  private Set<InstitutionEntity> institutionEntitySet;

  /**
   * Множество пособий города
   */
  @NonNull
  @ToString.Exclude
  @ManyToMany(mappedBy = "cityEntitySet")
  private Set<BenefitEntity> benefitEntitySet;

  /**
   * Конструктор для создания модели по ID
   * @param id ID города
   */
  public CityEntity(@NonNull BigInteger id) {
    this.id = id;
  }

  /**
   * Обработывает строковые поля объекта перед записью в базу данных
   * @param prepareFunc функция обработки строки
   * @return объект с обработанными полями
   */
  @Override
  public DBPreparer prepareForDB(Function<String, String> prepareFunc) {

    name = prepareFunc.apply(name);
    info = prepareFunc.apply(info);

    return this;
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    CityEntity cityEntity = (CityEntity) o;
    return Objects.equals(id, cityEntity.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
