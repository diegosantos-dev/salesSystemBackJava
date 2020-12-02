package br.com.vendas.Services.Impl;

import br.com.vendas.Controllers.Dto.ItemPedidoDTO;
import br.com.vendas.Controllers.Dto.PedidoDTO;
import br.com.vendas.Domain.Entity.Cliente;
import br.com.vendas.Domain.Entity.ItemPedido;
import br.com.vendas.Domain.Entity.Pedido;
import br.com.vendas.Domain.Entity.Produto;
import br.com.vendas.Domain.Enums.StatusPedido;
import br.com.vendas.Domain.Repositorys.ClienteRepository;
import br.com.vendas.Domain.Repositorys.ItemPedidoRepository;
import br.com.vendas.Domain.Repositorys.PedidoRepository;
import br.com.vendas.Domain.Repositorys.ProdutoRepository;
import br.com.vendas.Exception.RegraNegocioException;
import br.com.vendas.Services.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidosRepository;
    private final ClienteRepository clientesRepository;
    private final ProdutoRepository produtosRepository;
    private final ItemPedidoRepository itemsPedidoRepository;


    @Override
    @Transactional
    public Pedido salvar( PedidoDTO dto ) {
        Integer idCliente = dto.getCliente();
        Cliente cliente = clientesRepository
                .findById(idCliente)
                .orElseThrow(() -> new RegraNegocioException("Código de cliente inválido."));

        Pedido pedido = new Pedido();
        pedido.setDataPedido(LocalDate.now());
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.REALIZADO);

        List<ItemPedido> itemsPedido = converterItems(pedido, dto.getItens());
        pedido.setTotal(calcularTotalPedido(itemsPedido));
        pedidosRepository.save(pedido);
        itemsPedidoRepository.saveAll(itemsPedido);
        pedido.setItens(itemsPedido);
        return pedido;
    }

    @Override
    public Optional<Pedido> obterPedidoCompleto(Integer id) {
        return pedidosRepository.findByIdFetchItens(id);
    }

    @Override
    @Transactional
    public void atualizaStatus( Integer id, StatusPedido statusPedido ) {
        pedidosRepository
                .findById(id)
                .map( pedido -> {
                    pedido.setStatus(statusPedido);
                    return pedidosRepository.save(pedido);
                }).orElseThrow(() -> new RegraNegocioException("Pedido não encontrado"));
    }

    private List<ItemPedido> converterItems(Pedido pedido, List<ItemPedidoDTO> items){
        if(items.isEmpty()){
            throw new RegraNegocioException("Não é possível realizar um pedido sem items.");
        }

        return items
                .stream()
                .map( dto -> {
                    Integer idProduto = dto.getProduto();
                    Produto produto = produtosRepository
                            .findById(idProduto)
                            .orElseThrow(
                                    () -> new RegraNegocioException(
                                            "Código de produto inválido: "+ idProduto
                                    ));

                    ItemPedido itemPedido = new ItemPedido();
                    itemPedido.setQuantidade(dto.getQuantidade());
                    itemPedido.setPedido(pedido);
                    itemPedido.setProduto(produto);
                    return itemPedido;
                }).collect(Collectors.toList());

    }

    private BigDecimal calcularTotalPedido(List<ItemPedido> itemPedidos) {
       return itemPedidos.stream().map(itemPedido -> itemPedido.getProduto().getPreco().multiply(BigDecimal.valueOf(itemPedido.getQuantidade()))
        ).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
