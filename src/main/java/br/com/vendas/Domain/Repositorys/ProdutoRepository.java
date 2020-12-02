package br.com.vendas.Domain.Repositorys;

import br.com.vendas.Domain.Entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository  extends JpaRepository<Produto,Integer> {
}
