package com.example.familybenefits.service.s_interface;

import org.springframework.mail.MailException;

public interface EmailService {

  /**
   * Отправляет сообщение от имени сервиса пользователю по адресу с темой и текстом
   * @param to адрес получателя
   * @param subject тема сообщения
   * @param text текст сообщения
   * @throws MailException если не удалось отправить сообщение
   */
  void send(String to, String subject, String text) throws MailException;
}
