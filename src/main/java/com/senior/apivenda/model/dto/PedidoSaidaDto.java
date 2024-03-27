package com.senior.apivenda.model.dto;

import com.senior.apivenda.model.enums.FormaPagamentoEnum;
import com.senior.apivenda.model.enums.StatusPedidoEnum;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class PedidoSaidaDto implements Serializable {
    private UUID id;
    private StatusPedidoEnum statusPedidoEnum;
    private FormaPagamentoEnum formaPagamento;
    private Double percentualDesconto;
    private BigDecimal valorBruto;
    private BigDecimal valorDesconto;
    private BigDecimal valorLiquido;
    private LocalDateTime dataAlteracao;
    private LocalDateTime dataInclusao;
    private List<ItemSemPedidoSaidaDto> itens;
}
