package br.com.vendas.Controllers.Dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDTO {
    private Integer cliente;
    private List<ItemPedidoDTO> itens;
}
