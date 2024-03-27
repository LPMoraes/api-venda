package com.senior.apivenda.model.mapper;

import com.senior.apivenda.model.ItemPedido;
import com.senior.apivenda.model.dto.ItemPedidoDto;
import com.senior.apivenda.model.dto.ItemPedidoSaidaDto;
import com.senior.apivenda.model.dto.PedidoSemItemSaidaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ItemPedidoMapper {

    @Autowired
    private ProdutoMapper produtoMapper;

    public ItemPedido mapper(ItemPedidoDto dto){
        ItemPedido item = new ItemPedido();
        item.getPedido().setId(dto.getPedido().getId());
        item.getProduto().setId(dto.getProduto().getId());
        item.setDataInclusao(LocalDateTime.now());
        return item;
    }

    public ItemPedido mapperSemPedido(ItemPedidoDto dto){
        ItemPedido item = new ItemPedido();
        item.setId(dto.getIdItem());
        item.setProduto(produtoMapper.mapper(dto));
        item.setDataInclusao(LocalDateTime.now());
        return item;
    }

    public ItemPedidoSaidaDto mapperSemItemFilho(ItemPedido item){
        ItemPedidoSaidaDto dto = new ItemPedidoSaidaDto();
        dto.setId(item.getId());
        dto.setPedido(new PedidoSemItemSaidaDto(item.getPedido()));
        dto.setProduto(item.getProduto());
        dto.setDataAlteracao(item.getDataAlteracao());
        dto.setDataInclusao(item.getDataInclusao());
        return dto;
    }

    public ItemPedido mapper(ItemPedido itemDb, ItemPedidoDto entradaDto){
        ItemPedido item = new ItemPedido();
        item.setId(itemDb.getId());
        item.getPedido().setId(entradaDto.getPedido().getId());
        item.getProduto().setId(entradaDto.getProduto().getId());
        item.setDataAlteracao(LocalDateTime.now());
        item.setDataInclusao(itemDb.getDataInclusao());
        return item;
    }
}
