package com.senior.apivenda.model.dto;

import com.senior.apivenda.model.Produto;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ItemPedidoSaidaDto implements Serializable {
    private UUID id;
    private PedidoSemItemSaidaDto pedido;
    private Produto produto;
    private LocalDateTime dataAlteracao;
    private LocalDateTime dataInclusao;
}
