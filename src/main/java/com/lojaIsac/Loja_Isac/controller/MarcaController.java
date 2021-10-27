package com.lojaIsac.Loja_Isac.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import com.lojaIsac.Loja_Isac.model.Marca;
import com.lojaIsac.Loja_Isac.repository.MarcaRepository;
import java.util.Optional;
import javax.validation.Valid;


	
@Controller
public class MarcaController {
	
	@Autowired
	private MarcaRepository marcaRepository;
	
	
	@GetMapping("/administrativo/marcas/cadastrar")
	public ModelAndView cadastrar(Marca marca) {
		ModelAndView mv = new ModelAndView("administrativo/marcas/cadastro");
		mv.addObject("marca", marca);
		return mv;  
	}
	
	@GetMapping("/administrativo/marcas/listar")
	public ModelAndView listar() {
		ModelAndView mv = new ModelAndView("administrativo/marcas/lista");
		mv.addObject("listaMarcas", marcaRepository.findAll());
		return mv;
	}
	
	@GetMapping("/administrativo/marcas/editar/{id}")
	public ModelAndView editar(@PathVariable("id") Long id) {
		Optional<Marca> marca = marcaRepository.findById(id);
		return cadastrar(marca.get());
	}
	
	@GetMapping("/administrativo/marcas/remover/{id}")
	public ModelAndView remover(@PathVariable("id") Long id) {
		Optional<Marca> marca = marcaRepository.findById(id);
		marcaRepository.delete(marca.get());
		return listar();
	}
	
	@PostMapping("/administrativo/marcas/salvar")
	public ModelAndView salvar(@Valid Marca marca, BindingResult result) {
		if(result.hasErrors()) {
			return cadastrar(marca);
		}
		marcaRepository.saveAndFlush(marca);
		return cadastrar(new Marca());
	}
}
