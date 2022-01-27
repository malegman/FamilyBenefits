package com.example.familybenefits.dao.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

/**
 * Модель записи таблицы "user"
 */
@Entity
@Table(name = "user", schema = "family_benefit")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class UserEntity implements DBPreparer {

  /**
   * ID пользователя
   */
  @NonNull
  @Id
  @Column(name = "id")
  private BigInteger id;

  /**
   * Имя пользователя
   */
  @NonNull
  @Column(name = "name")
  private String name;

  /**
   * Эллектронная почта пользователя
   */
  @NonNull
  @Column(name = "email")
  private String email;

  /**
   * Статус подтверждения почты
   */
  @Column(name = "is_verified_email")
  private boolean isVerifiedEmail;

  /**
   * Пароль пользователя
   */
  @NonNull
  @Column(name = "password")
  private String password;

  /**
   * Дата рождения пользователя
   */
  @NonNull
  @Column(name = "date_birth")
  private Date dateBirth;

  /**
   * Дата подбора пособий пользователя
   */
  @NonNull
  @Column(name = "date_select_benefit")
  private Date dateSelectBenefit;

  /**
   * Город пользователя
   */
  @Nullable
  @ManyToOne
  @JoinColumn(name = "id_city")
  private CityEntity cityEntity;

  /**
   * Множество детей пользователя
   */
  @NonNull
  @ToString.Exclude
  @ManyToMany
  @JoinTable(
      name = "users_children", schema = "family_benefit",
      joinColumns = @JoinColumn(name = "id_user"),
      inverseJoinColumns = @JoinColumn(name = "id_child"))
  private Set<ChildEntity> childEntitySet;

  /**
   * Множество ролей пользователя
   */
  @NonNull
  @ToString.Exclude
  @ManyToMany
  @JoinTable(
      name = "users_roles", schema = "family_benefit",
      joinColumns = @JoinColumn(name = "id_user"),
      inverseJoinColumns = @JoinColumn(name = "id_role"))
  private Set<RoleEntity> roleEntitySet;

  /**
   * Добавляет пользователю роль по её названию
   * @param nameRole название добавляемой роли
   */
  public void addRole(String nameRole) {
    roleEntitySet.add(RoleEntity.builder().name(nameRole).build());
  }

  /**
   * Удаляет у пользователя роль по её названию
   * @param nameRole название удаляемой роли
   */
  public void removeRole(String nameRole) {

    RoleEntity roleEntityToRemove = null;

    for (RoleEntity roleEntity : roleEntitySet) {
      if (roleEntity.getName().equals(nameRole)) {
        roleEntityToRemove = roleEntity;
        break;
      }
    }

    if (roleEntityToRemove != null) {
      roleEntitySet.remove(roleEntityToRemove);
    }
  }

  /**
   * Обработывает строковые поля объекта перед записью в базу данных
   * @param prepareFunc функция обработки строки
   * @return объект с обработанными полями
   */
  @Override
  public DBPreparer prepareForDB(Function<String, String> prepareFunc) {

    name = prepareFunc.apply(name);
    email = prepareFunc.apply(email);
    password = prepareFunc.apply(password);

    return this;
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    UserEntity userEntity = (UserEntity) o;
    return Objects.equals(id, userEntity.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
