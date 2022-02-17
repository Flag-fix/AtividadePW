package com.lojaIsac.Loja_Isac.repository;

import com.lojaIsac.Loja_Isac.model.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MarcaRepository extends JpaRepository<Marca, Long>{

    @Query("from Marca m where m.nome like %?1% ")
    List<Marca> findByNome(String nome);

}
