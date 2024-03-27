package com.senior.apivenda.model.dto;

import com.senior.apivenda.model.enums.FormaPagamentoEnum;
import com.senior.apivenda.model.enums.StatusPedidoEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
public class PedidoDto implements Serializable {
    private UUID id;
    private FormaPagamentoEnum formaPagamento;
    private StatusPedidoEnum statusPedido;
    private Double percentualDesconto;
    private List<ItemPedidoDto> itens;
}
