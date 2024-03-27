package com.senior.apivenda.advice;

import com.senior.apivenda.advice.exception.*;
import com.senior.apivenda.model.dto.RespostaDto;
import com.senior.apivenda.service.RespostaService;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleInvalidArgument(MethodArgumentNotValidException exception) {
        Map<String, String> map =  new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(fieldError -> {
            map.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        return map;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Map<String, String> handleConstraintViolation(ConstraintViolationException exception) {
        Map<String, String> map =  new HashMap<>();
        exception.getConstraintViolations().forEach(fieldError -> {
            map.put(fieldError.getPropertyPath().toString(), fieldError.getMessage());
        });
        return map;
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(DemandaNaoEncontradaException.class)
    public RespostaDto handleDemandaNaoEncontrada(DemandaNaoEncontradaException exception) {
        return RespostaDto.error(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(FormaPagamentoNaoEncontradoException.class)
    public RespostaDto handleFormaPagamentoNaoEncontrado(FormaPagamentoNaoEncontradoException exception) {
        return RespostaDto.error(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ItemNaoEncontradoException.class)
    public RespostaDto handleItemNaoEncontrado(ItemNaoEncontradoException exception) {
        return RespostaDto.error(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(PedidoNaoEncontradoException.class)
    public RespostaDto handlePedidoNaoEncontrado(PedidoNaoEncontradoException exception) {
        return RespostaDto.error(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ProdutoDisabledException.class)
    public RespostaDto handleProdutoDisabled(ProdutoDisabledException exception) {
        return RespostaDto.error(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProdutoNaoEncontradoException.class)
    public RespostaDto handleProdutoNaoEncontrado(ProdutoNaoEncontradoException exception) {
        return RespostaDto.error(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ProdutoRelecionadoAPedidoException.class)
    public RespostaDto handleProdutoRelecionadoAPedido(ProdutoRelecionadoAPedidoException exception) {
        return RespostaDto.error(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(StatusPedidoNaoEncontradaException.class)
    public RespostaDto handleStatusPedidoNaoEncontrada(StatusPedidoNaoEncontradaException exception) {
        return RespostaDto.error(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public RespostaDto handleHttpMessageNotReadable(HttpMessageNotReadableException exception) {
        return RespostaDto.error(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
