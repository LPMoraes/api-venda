package com.senior.apivenda.advice.exception;

public class PedidoNaoEncontradoException extends RuntimeException{
    public PedidoNaoEncontradoException(String msg){
        super(msg);
    }
}
