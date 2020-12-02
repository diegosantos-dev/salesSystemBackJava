package br.com.vendas.Domain.Repositorys;

import br.com.vendas.Domain.Entity.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Integer> {
}

