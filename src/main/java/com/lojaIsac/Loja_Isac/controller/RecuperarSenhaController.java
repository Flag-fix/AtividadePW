package com.lojaIsac.Loja_Isac.controller;

import com.lojaIsac.Loja_Isac.model.Funcionario;
import com.lojaIsac.Loja_Isac.repository.FuncionarioRepository;
import com.lojaIsac.Loja_Isac.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import static com.lojaIsac.Loja_Isac.constants.Constants.*;

@Controller
public class RecuperarSenhaController {


    @Autowired
    private EmailService enviarEmailService;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @GetMapping("/mensagem")
    public ModelAndView mensagem() {
        return new ModelAndView("/administrativo/email/mensagem");
    }

    @GetMapping("/recuperarSenha")
    public ModelAndView recuperarSenha() {
        return new ModelAndView("/administrativo/email/recuperacaoSenha");
    }

    @GetMapping("/alterarSenha")
    public ModelAndView alterarSenha() {
        return new ModelAndView("/administrativo/email/alterarSenha");
    }

    @PostMapping("/solicitarCodigo")
    public String solicitarCodigo(@RequestParam(name="email") String email, Model model){
        Funcionario funcionario = funcionarioRepository.findByEmail(email);
        if(funcionario != null){
            funcionario.setCodigoRecuperacao(RandomStringUtils.randomAlphanumeric(15).toUpperCase());
            funcionario.setDataCodigo(new Date());
            funcionarioRepository.save(funcionario);
            enviarEmailService.enviar(
                    email,
                    RECUPERACAO_DE_SENHA,
                    MSG_CODIGO_RECUPERACAI_ENVIAR +
                            funcionario.getCodigoRecuperacao());
            model.addAttribute("mensagem", MSG_CODIGO_RECUPERACAO_OK);
            return ALTERAR_SENHA;
        }
        model.addAttribute(MENSAGEM,MSG_EMAIL_NAO_ENCONTRADO);
        return ALTERAR_SENHA;
    }

    @PostMapping("/alterarSenha")
    public String alterarSenha(
            @RequestParam(name="email") String email,
            @RequestParam(name="codigoRecuperacao") String codigoRecuperacao,
            @RequestParam(name="senha") String senha, Model model){
        Funcionario funcionario = funcionarioRepository.findByEmailAndCodigoRecuperacao(email,codigoRecuperacao);
        if(funcionario != null){
            Date dif = new Date(new Date().getTime() - funcionario.getDataCodigo().getTime());
            if (dif.getTime() /1000 < 15000){
                funcionario.setCodigoRecuperacao(null);
                funcionario.setDataCodigo(null);
                funcionario.setSenha(new BCryptPasswordEncoder().encode(senha));
                funcionarioRepository.save(funcionario);
                model.addAttribute("mensagem", SENHA_ALTERADA);
                return MENSAGEM;
            }else{
                model.addAttribute("mensagem", CODIGO_EXPIRADO);
            }
        }
        model.addAttribute("mensagem", EMAIL_CODIGO_NAO_ENCONTRADO);
        return MENSAGEM;
    }


}
