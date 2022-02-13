package com.example.familybenefits.service.s_interface;

import com.example.familybenefits.exception.DateFormatException;
import com.example.familybenefits.exception.DateTimeException;

import java.time.LocalDate;
import java.util.Set;

/**
 * Интерфейс сервиса, который предоставляет методы для работы с датой и временем
 */
public interface DateTimeService {

  /**
   * Преобразует строку формата "dd.mm.yyyy" в дату
   * @param userBirth дата в строковом виде
   * @return преобразованная строка в формат даты
   * @throws DateFormatException если полученная строка не соответствует формату "dd.mm.yyyy"
   */
  LocalDate strToDate(String userBirth) throws DateFormatException;

  /**
   * Проверяет дату на предшествие текущей дате
   * @param dateCheck проверяемая дата
   * @throws DateTimeException если проверяемая дата позже текущей даты
   */
  void checkDateBeforeNow(LocalDate dateCheck) throws DateTimeException;

  /**
   * Проверяет множество дат на предшествие текущей дате
   * @param dateSet множество проверяемых дат
   * @throws DateTimeException если проверяемая дата позже текущей даты
   */
  void checkDateBeforeNow(Set<LocalDate> dateSet) throws DateTimeException;

  /**
   * Проверяет, был ли день рождения после проверяемой даты
   * @param dateBirth дата рождения
   * @param dateCheck проверяемая дата
   * @throws DateTimeException если день рождения был после проверяемой даты
   */
  void checkBirthdayBefore(LocalDate dateBirth, LocalDate dateCheck) throws DateTimeException;

  /**
   * Проверяет, был ли дни рождения после проверяемой даты
   * @param dateBirthSet множество дат рождения
   * @param dateCheck проверяемая дата
   * @throws DateTimeException если один из дней рождения был после проверяемой даты
   */
  void checkBirthdayBefore(Set<LocalDate> dateBirthSet, LocalDate dateCheck) throws DateTimeException;
}
