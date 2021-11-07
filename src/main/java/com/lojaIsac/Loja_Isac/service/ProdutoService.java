package com.lojaIsac.Loja_Isac.service;

import com.lojaIsac.Loja_Isac.model.Imagem;
import com.lojaIsac.Loja_Isac.model.Produto;
import com.lojaIsac.Loja_Isac.repository.ImagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ImagemRepository imagemRepository;

    public List<Produto> listarImagensProdutos(List<Produto> listaProdutos){
        List<Imagem> imagens = imagemRepository.findAll();

        for (Imagem img : imagens) {
            for(Produto prod: listaProdutos){
                if(img.getProduto().equals(prod)){
                    prod.setNomeImagem(img.getNome());
                }
            }
        }
        return listaProdutos;
    }


}
