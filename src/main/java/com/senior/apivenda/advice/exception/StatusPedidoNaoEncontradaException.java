package com.senior.apivenda.advice.exception;

public class StatusPedidoNaoEncontradaException extends RuntimeException{
    public StatusPedidoNaoEncontradaException(String message){
        super(message);
    }
}
