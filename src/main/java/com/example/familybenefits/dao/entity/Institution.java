package com.example.familybenefits.dao.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Objects;

/**
 * Модель таблицы "institution"
 */
@Entity
@Table(name = "institution", schema = "familybenefit")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Institution {

  /**
   * ID учреждения
   */
  @Id
  @Column(name = "id")
  private BigInteger id;

  /**
   * Название учреждения
   */
  @Column(name = "name")
  private String name;

  /**
   * Информация учреждения
   */
  @Column(name = "info")
  private String info;

  /**
   * Адрес учреждения
   */
  @Column(name = "address")
  private String address;

  /**
   * Телефон учреждения
   */
  @Column(name = "phone")
  private String phone;

  /**
   * Электронная почта учржедения
   */
  @Column(name = "email")
  private String email;

  /**
   * График работы учреждения
   */
  @Column(name = "schedule")
  private String schedule;

  /**
   * Город учреждения
   */
  @ManyToOne
  @JoinColumn(name = "id_city")
  private City city;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Institution that = (Institution) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
