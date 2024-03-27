package com.senior.apivenda.service;

import com.senior.apivenda.advice.exception.ItemNaoEncontradoException;
import com.senior.apivenda.model.ItemPedido;
import com.senior.apivenda.model.Pedido;
import com.senior.apivenda.model.Produto;
import com.senior.apivenda.model.dto.ItemPedidoDto;
import com.senior.apivenda.model.dto.ItemPedidoSaidaDto;
import com.senior.apivenda.model.dto.RespostaDto;
import com.senior.apivenda.model.mapper.ItemPedidoMapper;
import com.senior.apivenda.repository.ItemPedidoRepository;
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
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ItemPedidoServiceTest {
    @Mock
    ItemPedidoRepository itemPedidoRepository;
    @Mock
    ProdutoService produtoService;
    @Mock
    PedidoService pedidoService;
    @Mock
    ItemPedidoMapper itemPedidoMapper;
    @Mock
    RespostaService respostaService;
    @Mock
    private Pageable pageableMock;

    @InjectMocks
    ItemPedidoService itemPedidoService;

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
        when(itemPedidoRepository.findAll())
                .thenReturn(easyRandom.objects(ItemPedido.class, 3).collect(Collectors.toList()));
        when(itemPedidoMapper.mapperSemItemFilho(any())).thenReturn(new ItemPedidoSaidaDto());
        Page<ItemPedidoSaidaDto> result =
                itemPedidoService.buscarTodos(pageableMock);
        verify(itemPedidoRepository).findAll();
        Assertions.assertNotNull(result);
    }


    @Test
    void testBuscarPorId_thenThrownException() {
        Exception exception = assertThrows(
                ItemNaoEncontradoException.class,
                () -> itemPedidoService.buscarPorId(uuidStr)
        );
        assertTrue(String.format("ItemPedido uuid:'%s' não encontrado ", uuidStr)
                .contains(exception.getMessage()));
    }

    @Test
    void testBuscarPorId() {
        when(itemPedidoRepository.findById(any())).thenReturn(Optional.of(easyRandom.nextObject(ItemPedido.class)));
        when(itemPedidoMapper.mapperSemItemFilho(any())).thenReturn(new ItemPedidoSaidaDto());
        ItemPedidoSaidaDto result =
                itemPedidoService.buscarPorId(uuidStr);
        verify(itemPedidoRepository).findById(any());
        verify(itemPedidoMapper).mapperSemItemFilho(any());
        Assertions.assertEquals(new ItemPedidoSaidaDto(), result);
    }

    @Test
    void testSalvar() {
        when(itemPedidoMapper.mapper(any())).thenReturn(new ItemPedido());
        when(itemPedidoRepository.save(any())).thenReturn(new ItemPedido());
        when(produtoService.produtoPorId(anyString())).thenReturn(new Produto());
        when(pedidoService.pedidoPorId(anyString())).thenReturn(new Pedido());
        when(itemPedidoMapper.mapperSemItemFilho(any())).thenReturn(new ItemPedidoSaidaDto());
        ItemPedidoSaidaDto result =
                itemPedidoService.salvar(new ItemPedidoDto());
        verify(itemPedidoMapper).mapper(any());
        verify(itemPedidoRepository).save(any());
        verify(produtoService).produtoPorId(any());
        verify(pedidoService).pedidoPorId(any());
        verify(itemPedidoMapper).mapperSemItemFilho(any());
        Assertions.assertEquals(new ItemPedidoSaidaDto(), result);
    }

    @Test
    void testAtualizar() {
        when(itemPedidoRepository.findById(any())).thenReturn(Optional.of(new ItemPedido()));
        when(itemPedidoMapper.mapper(any(), any())).thenReturn(new ItemPedido());
        when(itemPedidoRepository.save(any())).thenReturn(new ItemPedido());
        when(produtoService.produtoPorId(anyString())).thenReturn(new Produto());
        when(pedidoService.pedidoPorId(anyString())).thenReturn(new Pedido());
        when(itemPedidoMapper.mapperSemItemFilho(any())).thenReturn(new ItemPedidoSaidaDto());
        ItemPedidoSaidaDto result =
                itemPedidoService.atualizar(uuidStr, new ItemPedidoDto());
        verify(itemPedidoRepository).findById(any());
        verify(itemPedidoMapper).mapper(any(), any());
        verify(itemPedidoRepository).save(any());
        verify(produtoService).produtoPorId(any());
        verify(pedidoService).pedidoPorId(any());
        verify(itemPedidoMapper).mapperSemItemFilho(any());
        Assertions.assertEquals(new ItemPedidoSaidaDto(), result);
    }

    @Test
    void testRemover() {
        when(itemPedidoRepository.findById(any())).thenReturn(Optional.of(new ItemPedido()));
        doNothing().when(itemPedidoRepository).deleteById(any());
        RespostaDto result =
                itemPedidoService.remover(uuidStr);
        verify(itemPedidoRepository).findById(any());
        verify(itemPedidoRepository).deleteById(any());
        Assertions.assertEquals(String.valueOf(HttpStatus.ACCEPTED.value()), result.getStatus());
        Assertions.assertEquals(String.format("ItemPedido uuid:'%s' removido com sucesso", uuidStr), result.getMessage());
    }
}