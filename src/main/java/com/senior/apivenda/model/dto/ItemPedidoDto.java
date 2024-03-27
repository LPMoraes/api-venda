package com.senior.apivenda.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class ItemPedidoDto implements Serializable {
    private UUID idItem;
    private PedidoDto pedido;
    private ProdutoDto produto;
}
