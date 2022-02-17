package com.lojaIsac.Loja_Isac.repository;


import com.lojaIsac.Loja_Isac.model.Imagem;
import com.lojaIsac.Loja_Isac.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImagemRepository extends JpaRepository<Imagem, Long> {
    List<Imagem> findImagensProdutoByProduto(Produto produto);
}