package com.lojaIsac.Loja_Isac.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class NegadoController {

    @GetMapping("/negado")
    public ModelAndView negado() {
        return new ModelAndView("/administrativo/negado");
    }

}
