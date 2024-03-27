package com.senior.apivenda.advice.exception;

public class ProdutoNaoEncontradoException extends RuntimeException{
    public ProdutoNaoEncontradoException(String msg){
        super(msg);
    }
}
