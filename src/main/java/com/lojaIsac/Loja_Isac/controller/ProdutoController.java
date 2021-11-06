package com.lojaIsac.Loja_Isac.controller;

import com.lojaIsac.Loja_Isac.constants.ConstantsImagens;
import com.lojaIsac.Loja_Isac.model.Imagem;
import com.lojaIsac.Loja_Isac.model.Produto;
import com.lojaIsac.Loja_Isac.repository.CategoriaRepository;
import com.lojaIsac.Loja_Isac.repository.ImagemRepository;
import com.lojaIsac.Loja_Isac.repository.MarcaRepository;
import com.lojaIsac.Loja_Isac.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.lojaIsac.Loja_Isac.constants.ConstantsImagens.CAMINHO_PASTA_IMAGENS;

@Controller
@RequestMapping("/administrativo/produtos")
public class ProdutoController {

    ConstantsImagens constantsImagens;

    @Autowired
    private ImagemRepository imagemRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private MarcaRepository marcaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping("/cadastrar")
    public ModelAndView cadastrar(Produto produto) {
        ModelAndView mv = new ModelAndView("administrativo/produtos/cadastro");
        mv.addObject("produto", produto);
        mv.addObject("listaMarcas", marcaRepository.findAll());
        mv.addObject("listaCategorias", categoriaRepository.findAll());
        return mv;
    }

    @GetMapping("/listar")
    public ModelAndView listar() {
        ModelAndView mv = new ModelAndView("administrativo/produtos/lista");
        List<Imagem> imagens = imagemRepository.findAll();
        List<Produto> produtos = produtoRepository.findAll();

        for (Imagem img : imagens) {
            for(Produto prod: produtos){
                if(img.getProduto().equals(prod)){
                    prod.setNomeImagem(img.getNome());
                }
            }
        }
        mv.addObject("listaProdutos", produtos);
        return mv;
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable("id") Long id) {
        Optional<Produto> produto = produtoRepository.findById(id);
        return cadastrar(produto.get());
    }

    @GetMapping("/remover/{id}")
    public ModelAndView remover(@PathVariable("id") Long id) {
        Optional<Produto> produto = produtoRepository.findById(id);
        produtoRepository.delete(produto.get());
        return listar();
    }

    @ResponseBody
    @GetMapping("/mostrarImagem/{imagem}")
    public byte[] retornarImagem(@PathVariable("imagem") String imagem) {
        if (imagem != null || imagem.trim().length() > 0) {
            File imagemArquivo = new File(CAMINHO_PASTA_IMAGENS + imagem);
            try {
                return Files.readAllBytes(imagemArquivo.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    @PostMapping("/salvar")
    public ModelAndView salvar(@Validated Produto produto, BindingResult result, @RequestParam("file") List<MultipartFile> arquivo) {

        if (result.hasErrors()) {
            return cadastrar(produto);
        }
        produtoRepository.saveAndFlush(produto);
        try {

            if (!arquivo.isEmpty()) {
                Imagem imagem = new Imagem();
                //Salvou Produto
                produtoRepository.saveAndFlush(produto);
                for (MultipartFile file : arquivo) {
                    byte[] bytes = file.getBytes();
                    // Caminho onde a imagem vai ser salva
                    Path caminho = Paths.get(CAMINHO_PASTA_IMAGENS + String.valueOf(produto.getId()) + file.getOriginalFilename());
                    Files.write(caminho, bytes);
                    imagem.setNome(String.valueOf(produto.getId() + file.getOriginalFilename()));
                    imagem.setProduto(produto);
                    imagemRepository.saveAndFlush(imagem);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return cadastrar(new Produto());
    }
}
