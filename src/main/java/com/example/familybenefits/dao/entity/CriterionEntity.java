package com.example.familybenefits.dao.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Objects;
import java.util.function.Function;

/**
 * Модель записи таблицы "criterion"
 */
@Entity
@Table(name = "criterion", schema = "family_benefit")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class CriterionEntity implements DBPreparer {

  /**
   * ID критерия
   */
  @NonNull
  @Id
  @Column(name = "id")
  private BigInteger id;

  /**
   * Название критерия
   */
  @NonNull
  @Column(name = "name")
  private String name;

  /**
   * Информация критерия
   */
  @NonNull
  @Column(name = "info")
  private String info;

  /**
   * Тип критерия критерия
   */
  @Nullable
  @ManyToOne
  @JoinColumn(name = "id_type", referencedColumnName = "id")
  private CriterionTypeEntity criterionTypeEntity;

  /**
   * Конструктор для создания модели по ID
   * @param id ID критерия
   */
  public CriterionEntity(@NonNull BigInteger id) {
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
    CriterionEntity criterionEntity = (CriterionEntity) o;
    return Objects.equals(id, criterionEntity.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
