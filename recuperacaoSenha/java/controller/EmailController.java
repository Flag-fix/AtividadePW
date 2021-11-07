package com.lojaIsac.Loja_Isac.controller;

import com.lojaIsac.Loja_Isac.service.EnviarEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
public class EmailController {

    @Autowired
    private EnviarEmailService enviarEmailService;

    @GetMapping("/enviar-email")
    public String enviarEmail() {
        return enviarEmailService.enviar(
                "isac.bandeir4@gmail.com",
                "Recuperação de Senha",
                "Sua senha é 123");
    }
}
