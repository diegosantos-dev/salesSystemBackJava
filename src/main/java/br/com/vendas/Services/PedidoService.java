package br.com.vendas.Services;

import br.com.vendas.Controllers.Dto.PedidoDTO;
import br.com.vendas.Domain.Entity.Pedido;
import br.com.vendas.Domain.Enums.StatusPedido;

import java.util.Optional;

public interface PedidoService {

    Pedido salvar( PedidoDTO dto );
    Optional<Pedido> obterPedidoCompleto(Integer id);
    void atualizaStatus(Integer id, StatusPedido statusPedido);

}
