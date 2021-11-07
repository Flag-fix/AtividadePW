package com.lojaIsac.Loja_Isac.controller;

import com.lojaIsac.Loja_Isac.constants.Constants;
import com.lojaIsac.Loja_Isac.model.Categoria;
import com.lojaIsac.Loja_Isac.model.Imagem;
import com.lojaIsac.Loja_Isac.model.Marca;
import com.lojaIsac.Loja_Isac.model.Produto;
import com.lojaIsac.Loja_Isac.repository.CategoriaRepository;
import com.lojaIsac.Loja_Isac.repository.ImagemRepository;
import com.lojaIsac.Loja_Isac.repository.MarcaRepository;
import com.lojaIsac.Loja_Isac.repository.ProdutoRepository;
import com.lojaIsac.Loja_Isac.service.ProdutoService;
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

import static com.lojaIsac.Loja_Isac.constants.Constants.CAMINHO_PASTA_IMAGENS;

@Controller
@RequestMapping("/administrativo/produtos")
public class ProdutoController {

    Constants constantsImagens;

    @Autowired
    private ImagemRepository imagemRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private MarcaRepository marcaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProdutoService prodosService;

    @GetMapping("/cadastrar")
    public ModelAndView cadastrar(Produto produto) {
        ModelAndView mv = new ModelAndView("administrativo/produtos/cadastro");
        mv.addObject("produto", produto);
        mv.addObject("listaMarcas", marcaRepository.findAll());
        mv.addObject("listaCategorias", categoriaRepository.findAll());
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

    @GetMapping("/listar")
    public ModelAndView listar() {
        ModelAndView mv = new ModelAndView("administrativo/produtos/lista");
        List<Produto> produtos = produtoRepository.findAll();
        prodosService.listarImagensProdutos(produtos);
        mv.addObject("listaProdutos", produtos);
        return mv;
    }

    @GetMapping("/listar/descricao")
    public ModelAndView listarPorDescricao(String descricao) {
        ModelAndView mv = new ModelAndView("administrativo/produtos/lista");
        List<Produto> produtos = produtoRepository.findByDescricao(descricao);
        prodosService.listarImagensProdutos(produtos);
        mv.addObject("listaProdutos", produtos);
        return mv;
    }

    @GetMapping("/listar/categoria") //TODO - TENTA IMPLEMENTA NUM SERVICE
    public ModelAndView listarPorCategoria(String nome) {

        List<Categoria> listCategoria =  categoriaRepository.findByNome(nome);
        List<Produto> listProduto = new ArrayList<>();

        for(Categoria categoria : listCategoria){
            listProduto = produtoRepository.findByCategoria(categoria);
        }
        prodosService.listarImagensProdutos(listProduto);
        ModelAndView mv = new ModelAndView("administrativo/produtos/lista");
        mv.addObject("listaProdutos",  listProduto);
        return mv;
    }

    @GetMapping("/listar/marca")
    public ModelAndView listarPorMarca(String nome) {
        List<Produto> listProduto = new ArrayList<>();
        List<Marca> listMarca = marcaRepository.findByNome(nome);

        for(Marca marca : listMarca){
            listProduto = produtoRepository.findByMarca(marca);
        }
        prodosService.listarImagensProdutos(listProduto);
        ModelAndView mv = new ModelAndView("administrativo/produtos/lista");
        mv.addObject("listaProdutos", listProduto);
        return mv;
    }

}
