package com.senior.apivenda.model.enums;

import com.senior.apivenda.advice.exception.DemandaNaoEncontradaException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum TipoDemandaEnum {
    PRODUTO(1, "Produto"),
    SERVICO(2, "Serviço");

    private Integer id;
    private String nome;

    public static Integer valueOfTipo(TipoDemandaEnum e){
        return Arrays.stream(TipoDemandaEnum.values())
                .filter(tipoDemandaEnum -> tipoDemandaEnum.equals(e))
                .findAny()
                .orElseThrow(() -> new DemandaNaoEncontradaException("TipoDemanda não encontrada"))
                .getId();

    }

    public static TipoDemandaEnum valueOfTipo(Integer id){
        return Arrays.stream(TipoDemandaEnum.values())
                .filter(tipoDemandaEnum -> tipoDemandaEnum.getId().equals(id))
                .findAny()
                .orElseThrow(() -> new DemandaNaoEncontradaException("TipoDemanda não encontrada"));
    }
}
