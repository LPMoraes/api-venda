package com.senior.apivenda.model.dto;

import com.senior.apivenda.model.Pedido;
import com.senior.apivenda.model.enums.FormaPagamentoEnum;
import com.senior.apivenda.model.enums.StatusPedidoEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoSemItemSaidaDto implements Serializable {
    private UUID id;
    private StatusPedidoEnum statusPedidoEnum;
    private FormaPagamentoEnum formaPagamento;
    private Double percentualDesconto;
    private BigDecimal valorBruto;
    private BigDecimal valorDesconto;
    private BigDecimal valorLiquido;
    private LocalDateTime dataAlteracao;
    private LocalDateTime dataInclusao;

    public PedidoSemItemSaidaDto(Pedido pedido){
        this.id = pedido.getId();
        this.statusPedidoEnum = StatusPedidoEnum.valueOfTipo(pedido.getStatusPedido());
        this.formaPagamento = FormaPagamentoEnum.valueOfTipo(pedido.getFormaPagamento());
        this.percentualDesconto = pedido.getPercentualDesconto();
        this.valorBruto = pedido.getValorBruto();
        this.valorDesconto = pedido.getValorDesconto();
        this.valorLiquido = pedido.getValorLiquido();
        this.dataAlteracao = pedido.getDataAlteracao();
        this.dataInclusao = pedido.getDataInclusao();
    }
}
