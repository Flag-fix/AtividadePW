package com.lojaIsac.Loja_Isac.repository;

import com.lojaIsac.Loja_Isac.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    Funcionario findByEmail(String email);

    Funcionario findByEmailAndCodigoRecuperacao(String email, String codigoRecuperacao);

}
