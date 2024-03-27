package com.senior.apivenda.model.enums;

import com.senior.apivenda.advice.exception.StatusPedidoNaoEncontradaException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum StatusPedidoEnum {
    ABERTO(1, "Aberto"),
    FECHADO(2, "Fechado");

    private Integer id;
    private String nome;

    public static Integer valueOfTipo(StatusPedidoEnum e){
        return Arrays.stream(StatusPedidoEnum.values())
                .filter(stPedido -> stPedido.equals(e))
                .findAny()
                .orElseThrow(() -> new StatusPedidoNaoEncontradaException("StatusPedido não encontrada"))
                .getId();

    }

    public static StatusPedidoEnum valueOfTipo(Integer id){
        return Arrays.stream(StatusPedidoEnum.values())
                .filter(stPedido -> stPedido.getId().equals(id))
                .findAny()
                .orElseThrow(() -> new StatusPedidoNaoEncontradaException("StatusPedido não encontrada"));
    }
}
