package com.nakaligoba.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Value("${spring.mail.host}")
    public String host;

    @Value("${spring.mail.port}")
    public int port;

    @Value("${spring.mail.username}")
    public String username;

    @Value("${spring.mail.password}")
    public String password;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.ssl.enable", true);
        properties.put("mail.smtp.starttls.enable", true);

        properties.put("mail.smtp.socks.host", "krmp-proxy.9rum.cc");
        properties.put("mail.smtp.socks.port", "3128");

        return mailSender;
    }
}
