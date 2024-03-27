package com.senior.apivenda.model.mapper;

import com.senior.apivenda.model.Produto;
import com.senior.apivenda.model.dto.ItemPedidoDto;
import com.senior.apivenda.model.dto.ProdutoDto;
import com.senior.apivenda.model.dto.ProdutoSaidaDto;
import com.senior.apivenda.model.enums.TipoDemandaEnum;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ProdutoMapper {
    public Produto mapper(ProdutoDto dto){
        Produto produto = new Produto();
        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setValorUnitario(dto.getValor());
        produto.setQuantidade(dto.getQuantidade());
        produto.setTipoDemanda(TipoDemandaEnum.valueOfTipo(dto.getTipoDemanda()));
        produto.setDataInclusao(LocalDateTime.now());
        produto.setAtivo(true);
        return produto;
    }

    public void mapper(Produto produto, ProdutoDto dto){
        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setValorUnitario(dto.getValor());
        produto.setQuantidade(dto.getQuantidade());
        produto.setTipoDemanda(TipoDemandaEnum.valueOfTipo(dto.getTipoDemanda()));
        produto.setDataAlteracao(LocalDateTime.now());
        produto.setAtivo(dto.getAtivo());
    }

    public ProdutoSaidaDto mapper(Produto produto){
        ProdutoSaidaDto dto = new ProdutoSaidaDto();
        dto.setId(produto.getId());
        dto.setNome(produto.getNome());
        dto.setDescricao(produto.getDescricao());
        dto.setValor(produto.getValorUnitario());
        dto.setQuantidade(produto.getQuantidade());
        dto.setTipoDemanda(TipoDemandaEnum.valueOfTipo(produto.getTipoDemanda()));
        dto.setDataAlteracao(produto.getDataAlteracao());
        dto.setDataInclusao(produto.getDataInclusao());
        dto.setAtivo(produto.getAtivo());
        return dto;
    }

    public Produto mapper(ItemPedidoDto itemDto){
        Produto produto = new Produto();
        ProdutoDto dto = itemDto.getProduto();
        produto.setId(dto.getId());
        produto.setNome(dto.getNome());
        produto.setValorUnitario(dto.getValor());
        produto.setTipoDemanda(TipoDemandaEnum.valueOfTipo(dto.getTipoDemanda()));
        produto.setAtivo(dto.getAtivo());
        return produto;
    }
}
