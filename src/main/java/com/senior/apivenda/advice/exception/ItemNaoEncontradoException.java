package com.senior.apivenda.advice.exception;

public class ItemNaoEncontradoException extends RuntimeException{
    public ItemNaoEncontradoException(String msg){
        super(msg);
    }
}
