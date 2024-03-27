package com.senior.apivenda.service;

import com.senior.apivenda.advice.exception.ProdutoNaoEncontradoException;
import com.senior.apivenda.advice.exception.ProdutoRelecionadoAPedidoException;
import com.senior.apivenda.model.Produto;
import com.senior.apivenda.model.dto.ProdutoDto;
import com.senior.apivenda.model.dto.ProdutoSaidaDto;
import com.senior.apivenda.model.dto.RespostaDto;
import com.senior.apivenda.model.mapper.ProdutoMapper;
import com.senior.apivenda.repository.ProdutoRepository;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ProdutoServiceTest {
    @Mock
    ProdutoRepository produtoRepository;
    @Mock
    ProdutoMapper produtoMapper;
    @Mock
    RespostaService respostaService;

    @Mock
    private Pageable pageableMock;

    @Mock
    private Page<Produto> pageProduto;

    @InjectMocks
    ProdutoService produtoService;

    private EasyRandom easyRandom;
    private String uuidStr;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        easyRandom = new EasyRandom();
        uuidStr = easyRandom.nextObject(UUID.class).toString();
    }

    @Test
    void testBuscarTodos() {
        when(produtoRepository.findAll(pageableMock)).thenReturn(pageProduto);
        Page<Produto> result =
                produtoService.buscarTodos(pageableMock);
        verify(produtoRepository).findAll(pageableMock);
        Assertions.assertEquals(pageProduto, result);
    }

    @Test
    void testBuscarPorId_thenThrownException() {
        Exception exception = assertThrows(
                ProdutoNaoEncontradoException.class,
                () -> produtoService.buscarPorId(uuidStr)
        );
        assertTrue(String.format("Produto uuid:'%s' não encontrado ", uuidStr)
                .contains(exception.getMessage()));
    }

    @Test
    void testBuscarPorId() {
        when(produtoRepository.findById(any())).thenReturn(Optional.of(easyRandom.nextObject(Produto.class)));
        when(produtoMapper.mapper((Produto) any())).thenReturn(new ProdutoSaidaDto());
        ProdutoSaidaDto result =
                produtoService.buscarPorId(uuidStr);
        verify(produtoRepository).findById(any());
        verify(produtoMapper).mapper((Produto) any());
        Assertions.assertEquals(new ProdutoSaidaDto(), result);
    }

    @Test
    void testBuscarTodosAtivos() {
        when(produtoRepository.findAllIsAtivo(pageableMock)).thenReturn(pageProduto);
        Page<Produto> result =
                produtoService.buscarTodosAtivos(pageableMock);
        verify(produtoRepository).findAllIsAtivo(pageableMock);
        Assertions.assertEquals(pageProduto, result);
    }

    @Test
    void testSalvar() {
        when(produtoMapper.mapper((ProdutoDto) any())).thenReturn(new Produto());
        when(produtoRepository.save(any())).thenReturn(new Produto());
        when(produtoMapper.mapper((Produto) any())).thenReturn(new ProdutoSaidaDto());
        ProdutoSaidaDto result =
                produtoService.salvar(new ProdutoDto());
        verify(produtoRepository).save(any());
        verify(produtoMapper).mapper((ProdutoDto) any());
        verify(produtoMapper).mapper((Produto) any());
        Assertions.assertEquals(new ProdutoSaidaDto(), result);
    }

    @Test
    void testAtualizar() {
        when(produtoRepository.findById(any())).thenReturn(Optional.of(easyRandom.nextObject(Produto.class)));
        doNothing().when(produtoMapper).mapper(any(), any());
        when(produtoRepository.save(any())).thenReturn(new Produto());
        when(produtoMapper.mapper((Produto) any())).thenReturn(new ProdutoSaidaDto());
        ProdutoSaidaDto result =
                produtoService.atualizar(uuidStr, new ProdutoDto());
        verify(produtoRepository).findById(any());
        verify(produtoRepository).save(any());
        verify(produtoMapper).mapper(any(), any());
        verify(produtoMapper).mapper((Produto) any());
        Assertions.assertEquals(new ProdutoSaidaDto(), result);
    }

    @Test
    void testRemover_thenThrownException() {
        when(produtoRepository.findById(any())).thenReturn(Optional.of(new Produto()));
        when(produtoRepository.isPedido(any())).thenReturn(Optional.of(new Produto()));
        Exception exception = assertThrows(
                ProdutoRelecionadoAPedidoException.class,
                () -> produtoService.remover(uuidStr)
        );
        assertTrue(String.format("Não será possível remover Produto uuid:'%s' pois o mesmo está relacionado a um pedido", uuidStr)
                .contains(exception.getMessage()));
    }

    @Test
    void testRemover() {
        when(produtoRepository.findById(any())).thenReturn(Optional.of(new Produto()));
        when(produtoRepository.isPedido(any())).thenReturn(Optional.empty());
        doNothing().when(produtoRepository).deleteById(any());
        RespostaDto resultObjt =
                produtoService.remover(uuidStr);
        verify(produtoRepository).findById(any());
        verify(produtoRepository).isPedido(any());
        verify(produtoRepository).deleteById(any());
        Assertions.assertEquals(String.valueOf(HttpStatus.ACCEPTED.value()), resultObjt.getStatus());
        Assertions.assertEquals(String.format("Produto uuid:'%s' removido com sucesso", uuidStr), resultObjt.getMessage());
    }
}