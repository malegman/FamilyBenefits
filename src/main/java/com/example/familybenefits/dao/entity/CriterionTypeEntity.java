package com.example.familybenefits.dao.entity;

import com.example.familybenefits.security.service.s_interface.EntityPreparer;
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
 * Модель записи таблицы "criterion_type"
 */
@Entity
@Table(name = "criterion_type", schema = "family_benefit")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class CriterionTypeEntity implements EntityPreparer {

  /**
   * ID типа критерия
   */
  @NonNull
  @Id
  @Column(name = "id")
  private BigInteger id;

  /**
   * Название типа критерия
   */
  @NonNull
  @Column(name = "name")
  private String name;

  /**
   * Информация типа критерия
   */
  @NonNull
  @Column(name = "info")
  private String info;

  /**
   * Множество критерий типа критерия
   */
  @NonNull
  @ToString.Exclude
  @OneToMany(mappedBy = "criterionTypeEntity")
  private Set<CriterionEntity> criterionEntitySet;

  /**
   * Конструктор для создания модели по ID
   * @param id ID типа критерия
   */
  public CriterionTypeEntity(@NonNull BigInteger id) {
    this.id = id;
  }

  /**
   * Обрабатывает строковые поля объекта перед записью в базу данных
   * @param prepareFunc функция обработки строки
   * @return объект с обработанными полями
   */
  @Override
  public EntityPreparer prepareForDB(Function<String, String> prepareFunc) {

    name = prepareFunc.apply(name);
    info = prepareFunc.apply(info);

    return this;
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    CriterionTypeEntity that = (CriterionTypeEntity) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}

