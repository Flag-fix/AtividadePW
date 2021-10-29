package com.lojaIsac.Loja_Isac.controller;

import com.lojaIsac.Loja_Isac.model.Cidade;
import com.lojaIsac.Loja_Isac.repository.CidadeRepository;
import com.lojaIsac.Loja_Isac.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/administrativo/cidades")
public class CidadeController {

	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private EstadoRepository estadoRepository;

	@GetMapping("/cadastrar")
	public ModelAndView cadastrar(Cidade cidade) {
		ModelAndView mv =  new ModelAndView("administrativo/cidades/cadastro");
		mv.addObject("cidade", cidade);
		mv.addObject("listaEstados", estadoRepository.findAll());
		return mv;
	}

	@GetMapping("/listar")
	public ModelAndView listar() {
		ModelAndView mv=new ModelAndView("administrativo/cidades/lista");
		return mv.addObject("listaCidades", cidadeRepository.findAll());
	}

	@GetMapping("/editar/{id}")
	public ModelAndView editar(@PathVariable("id") Long id) {
		Optional<Cidade> cidade = cidadeRepository.findById(id);
		return cadastrar(cidade.get());
	}

	@GetMapping("/remover/{id}")
	public ModelAndView remover(@PathVariable("id") Long id) {
		Optional<Cidade> cidade = cidadeRepository.findById(id);
		cidadeRepository.delete(cidade.get());
		return listar();
	}

	
	@PostMapping("/salvar")
	public ModelAndView salvar(@Validated Cidade cidade, BindingResult result) {
		
		if(result.hasErrors()) {
			return cadastrar(cidade);
		}
		cidadeRepository.saveAndFlush(cidade);
		
		return cadastrar(new Cidade());
	}
}
