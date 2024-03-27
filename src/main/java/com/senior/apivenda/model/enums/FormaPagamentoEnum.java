package com.senior.apivenda.model.enums;

import com.senior.apivenda.advice.exception.FormaPagamentoNaoEncontradoException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum FormaPagamentoEnum {

    BOLETO(1, "Boleto", "Boleto Bancário"),
    CARTAO_CREDITO(2, "Cartão de Crédito", "Parcelamento em 10 vezes"),
    CARTAO_DEBITO(3, "Cartão de Débito", "Débito em conta"),
    PIX(4, "PIX", "Via QRCore ou Chave PIX");
    private Integer id;
    private String nome;
    private String descricao;


    public static Integer valueOfTipo(FormaPagamentoEnum e){
        return Arrays.stream(FormaPagamentoEnum.values())
                .filter(fPagamento -> fPagamento.equals(e))
                .findAny()
                .orElseThrow(() -> new FormaPagamentoNaoEncontradoException("FormaPagamento não encontrada"))
                .getId();

    }

    public static FormaPagamentoEnum valueOfTipo(Integer id){
        return Arrays.stream(FormaPagamentoEnum.values())
                .filter(fPagamento -> fPagamento.getId().equals(id))
                .findAny()
                .orElseThrow(() -> new FormaPagamentoNaoEncontradoException("FormaPagamento não encontrada"));
    }

}
