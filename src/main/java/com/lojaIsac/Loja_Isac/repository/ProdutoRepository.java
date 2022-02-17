package com.lojaIsac.Loja_Isac.repository;

import com.lojaIsac.Loja_Isac.model.Categoria;
import com.lojaIsac.Loja_Isac.model.Marca;
import com.lojaIsac.Loja_Isac.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long>{

    @Query("from Produto p where p.descricao like %?1% ")
    List<Produto> findByDescricao(String descricao);

    List<Produto> findByCategoria(Categoria categoria);

    List<Produto> findByMarca(Marca marca);


}
