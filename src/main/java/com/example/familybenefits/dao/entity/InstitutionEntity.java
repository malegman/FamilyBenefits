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
 * Модель таблицы "institution"
 */
@Entity
@Table(name = "institution", schema = "familybenefit")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class InstitutionEntity implements DBPreparer {

  /**
   * ID учреждения
   */
  @NonNull
  @Id
  @Column(name = "id")
  private BigInteger id;

  /**
   * Название учреждения
   */
  @NonNull
  @Column(name = "name")
  private String name;

  /**
   * Информация учреждения
   */
  @Nullable
  @Column(name = "info")
  private String info;

  /**
   * Адрес учреждения
   */
  @NonNull
  @Column(name = "address")
  private String address;

  /**
   * Телефон учреждения
   */
  @Nullable
  @Column(name = "phone")
  private String phone;

  /**
   * Электронная почта учржедения
   */
  @Nullable
  @Column(name = "email")
  private String email;

  /**
   * График работы учреждения
   */
  @Nullable
  @Column(name = "schedule")
  private String schedule;

  /**
   * Город учреждения
   */
  @NonNull
  @ManyToOne
  @JoinColumn(name = "id_city")
  private CityEntity cityEntity;

  /**
   * Конструктор для создания модели по ID
   * @param id ID учреждения
   */
  public InstitutionEntity(@NonNull BigInteger id) {
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
    address = prepareFunc.apply(address);
    phone = prepareFunc.apply(phone);
    email = prepareFunc.apply(email);
    schedule = prepareFunc.apply(schedule);

    return this;
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    InstitutionEntity that = (InstitutionEntity) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
