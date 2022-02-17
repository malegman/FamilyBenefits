package com.example.familybenefits.service.implementation;

import com.example.familybenefits.resource.RMail;
import com.example.familybenefits.service.s_interface.EmailService;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;

/**
 * Реализация сервиса для отправки сообщений на электронную почту
 */
@Service
public class EmailServiceFB implements EmailService {

  /**
   * Почтовый сервис
   */
  private static final JavaMailSenderImpl mailSender;

  // инициализация и настройка почтового сервиса
  static {
    mailSender = new JavaMailSenderImpl();

    mailSender.setHost(RMail.HOST);
    mailSender.setPort(RMail.PORT);
    mailSender.setUsername(RMail.USERNAME);
    mailSender.setPassword(RMail.PASSWORD);

    Properties props = mailSender.getJavaMailProperties();
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.debug", "true");
  }

  /**
   * Отправляет сообщение от имени сервиса пользователю по адресу с темой и текстом
   * @param to адрес получателя
   * @param subject тема сообщения
   * @param text текст сообщения
   * @throws MailException если не удалось отправить сообщение
   */
  @Override
  public void send(String to, String subject, String text) throws MailException {

    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(to);
    message.setSubject(subject);
    message.setText(text);

    mailSender.send(message);
  }
}
