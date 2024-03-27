package com.senior.apivenda.advice.exception;

public class ProdutoDisabledException extends RuntimeException{
    public ProdutoDisabledException(String msg){
        super(msg);
    }
}
