package com.lojaIsac.Loja_Isac.service;


import com.lojaIsac.Loja_Isac.model.Cliente;
import com.lojaIsac.Loja_Isac.model.ItensCompra;

import java.util.List;

public interface CarrinhoService {

    List<ItensCompra> calcularTotal(List<ItensCompra> listItensCompras);

    Cliente buscarUsuarioLogado(Cliente cliente);
}