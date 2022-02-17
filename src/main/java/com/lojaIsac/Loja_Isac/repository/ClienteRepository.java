package com.lojaIsac.Loja_Isac.repository;


import com.lojaIsac.Loja_Isac.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long>{

    @Query("from Cliente where email=?1")
    public List<Cliente> buscarClienteEmail(String email);
}
