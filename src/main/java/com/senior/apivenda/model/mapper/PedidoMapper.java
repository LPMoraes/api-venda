package com.senior.apivenda.model.mapper;

import com.senior.apivenda.model.ItemPedido;
import com.senior.apivenda.model.Pedido;
import com.senior.apivenda.model.dto.ItemSemPedidoSaidaDto;
import com.senior.apivenda.model.dto.PedidoDto;
import com.senior.apivenda.model.dto.PedidoSaidaDto;
import com.senior.apivenda.model.enums.FormaPagamentoEnum;
import com.senior.apivenda.model.enums.StatusPedidoEnum;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PedidoMapper {

    public Pedido mapper(PedidoDto dto){
        Pedido pedido = new Pedido();
        pedido.setFormaPagamento(FormaPagamentoEnum.valueOfTipo(dto.getFormaPagamento()));
        pedido.setStatusPedido(StatusPedidoEnum.valueOfTipo(dto.getStatusPedido()));
        pedido.setPercentualDesconto(dto.getPercentualDesconto());
        pedido.setDataInclusao(LocalDateTime.now());
        pedido.setItens(new ArrayList<>());
        return pedido;
    }

    public PedidoSaidaDto mapper(Pedido pedido, List<ItemPedido> itens){
        PedidoSaidaDto dto = new PedidoSaidaDto();
        dto.setId(pedido.getId());
        dto.setStatusPedidoEnum(StatusPedidoEnum.valueOfTipo(pedido.getStatusPedido()));
        dto.setFormaPagamento(FormaPagamentoEnum.valueOfTipo(pedido.getFormaPagamento()));
        dto.setPercentualDesconto(pedido.getPercentualDesconto());
        dto.setValorBruto(pedido.getValorBruto());
        dto.setValorDesconto(pedido.getValorDesconto());
        dto.setValorLiquido(pedido.getValorLiquido());
        dto.setDataAlteracao(pedido.getDataAlteracao());
        dto.setDataInclusao(pedido.getDataInclusao());
        dto.setItens(itens.stream()
                .map(this::itemSemPedido)
                .collect(Collectors.toList()));
        return dto;
    }

    private ItemSemPedidoSaidaDto itemSemPedido(ItemPedido itemPedido){
        ItemSemPedidoSaidaDto saidaDto =  new ItemSemPedidoSaidaDto();
        saidaDto.setIdItem(itemPedido.getId());
        saidaDto.setProduto(itemPedido.getProduto());
        saidaDto.setDataAlteracao(itemPedido.getDataAlteracao());
        saidaDto.setDataInclusao(itemPedido.getDataInclusao());
        return saidaDto;
    }

    public void mapper(Pedido pedido, PedidoDto dto){
        pedido.setFormaPagamento(FormaPagamentoEnum.valueOfTipo(dto.getFormaPagamento()));
        pedido.setStatusPedido(StatusPedidoEnum.valueOfTipo(dto.getStatusPedido()));
        pedido.setPercentualDesconto(dto.getPercentualDesconto());
        pedido.setDataAlteracao(LocalDateTime.now());
    }
}
