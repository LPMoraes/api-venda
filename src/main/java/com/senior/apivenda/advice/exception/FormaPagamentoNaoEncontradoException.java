package com.senior.apivenda.advice.exception;

public class FormaPagamentoNaoEncontradoException extends RuntimeException{
    public FormaPagamentoNaoEncontradoException(String msg){
        super(msg);
    }
}
