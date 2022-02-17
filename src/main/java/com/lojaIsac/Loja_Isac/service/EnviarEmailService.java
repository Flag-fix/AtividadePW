package com.lojaIsac.Loja_Isac.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EnviarEmailService {

    @Autowired
    public JavaMailSender mailSender;


    public String enviar(String destinatario, String assunto, String mensagemCorpo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("isac.bandeir4@gmail.com");
        message.setTo(destinatario);
        message.setSubject(assunto);
        message.setText(mensagemCorpo);
        try {
            mailSender.send(message);
            return "Email enviado com sucesso!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao enviar email.";
        }
    }
}
