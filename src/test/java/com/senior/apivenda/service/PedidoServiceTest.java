package com.senior.apivenda.service;

import com.senior.apivenda.advice.exception.PedidoNaoEncontradoException;
import com.senior.apivenda.advice.exception.ProdutoDisabledException;
import com.senior.apivenda.advice.exception.ProdutoNaoEncontradoException;
import com.senior.apivenda.model.ItemPedido;
import com.senior.apivenda.model.Pedido;
import com.senior.apivenda.model.Produto;
import com.senior.apivenda.model.dto.ItemPedidoDto;
import com.senior.apivenda.model.dto.PedidoDto;
import com.senior.apivenda.model.dto.PedidoSaidaDto;
import com.senior.apivenda.model.dto.RespostaDto;
import com.senior.apivenda.model.enums.StatusPedidoEnum;
import com.senior.apivenda.model.enums.TipoDemandaEnum;
import com.senior.apivenda.model.mapper.ItemPedidoMapper;
import com.senior.apivenda.model.mapper.PedidoMapper;
import com.senior.apivenda.repository.ItemPedidoRepository;
import com.senior.apivenda.repository.PedidoRepository;
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

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class PedidoServiceTest {
    @Mock
    PedidoRepository pedidoRepository;
    @Mock
    ItemPedidoRepository itemRepository;
    @Mock
    ProdutoRepository produtoRepository;
    @Mock
    PedidoMapper pedidoMapper;
    @Mock
    ItemPedidoMapper itemMapper;

    @Mock
    RespostaService respostaService;

    @Mock
    private Pageable pageableMock;

    @Mock
    private Page<Pedido> pagePedido;

    @InjectMocks
    PedidoService pedidoService;

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
        when(pedidoRepository.findAllAsc(pageableMock)).thenReturn(pagePedido);
        Page<Pedido> result =
                pedidoService.buscarTodos(pageableMock);
        verify(pedidoRepository).findAllAsc(pageableMock);
        Assertions.assertEquals(pagePedido, result);
    }

    @Test
    void testBuscarPorId_thenThrownException() {
        Exception exception = assertThrows(
                PedidoNaoEncontradoException.class,
                () -> pedidoService.buscarPorId(uuidStr)
        );
        assertTrue(String.format("Pedido uuid:'%s' não encontrado ", uuidStr)
                .contains(exception.getMessage()));
    }

    @Test
    void testBuscarPorId() {
        when(pedidoRepository.findById(any())).thenReturn(Optional.of(new Pedido()));
        when(pedidoMapper.mapper(any(), (List<ItemPedido>) any())).thenReturn(new PedidoSaidaDto());
        PedidoSaidaDto result =
                pedidoService.buscarPorId(uuidStr);
        verify(pedidoRepository).findById(any());
        verify(pedidoMapper).mapper(any(), (List<ItemPedido>) any());
        Assertions.assertEquals(new PedidoSaidaDto(), result);
    }

    @Test
    void testSalvar_ProdutoNaoEncontradoException() {
        ItemPedido itemPedido = easyRandom.nextObject(ItemPedido.class);
        itemPedido.setProduto(easyRandom.nextObject(Produto.class));
        when(pedidoMapper.mapper(any())).thenReturn(new Pedido());
        when(itemMapper.mapperSemPedido(any())).thenReturn(itemPedido);
        when(produtoRepository.findById(any())).thenReturn(Optional.empty());
        Exception exception = assertThrows(
                ProdutoNaoEncontradoException.class,
                () -> pedidoService.salvar(easyRandom.nextObject(PedidoDto.class))
        );
        assertTrue(String.format("Produto uuid:'%s' não encontrado ", itemPedido.getProduto().getId())
                .contains(exception.getMessage()));
    }

    @Test
    void testSalvar_ProdutoDisabledException() {
        Produto produto = easyRandom.nextObject(Produto.class);
        ItemPedido itemPedido = easyRandom.nextObject(ItemPedido.class);
        itemPedido.setProduto(produto);
        itemPedido.getProduto().setAtivo(false);
        when(pedidoMapper.mapper(any())).thenReturn(easyRandom.nextObject(Pedido.class));
        when(itemMapper.mapperSemPedido(any())).thenReturn(itemPedido);
        when(produtoRepository.findById(any())).thenReturn(Optional.of(produto));
        Exception exception = assertThrows(
                ProdutoDisabledException.class,
                () -> pedidoService.salvar(easyRandom.nextObject(PedidoDto.class))
        );
        assertTrue(String.format("Não foi possível realizar pedido pois Produto uuid:'%s' encontra-se desativado",
                        produto.getId())
                .contains(exception.getMessage()));
    }

    @Test
    void testSalvar_ComDesconto() {
        PedidoDto pedidoDto = easyRandom.nextObject(PedidoDto.class);
        pedidoDto.setItens(new ArrayList<>(Arrays.asList(easyRandom.nextObject(ItemPedidoDto.class))));
        Pedido pedido = easyRandom.nextObject(Pedido.class);
        pedido.setPercentualDesconto(Double.valueOf(5));
        pedido.setStatusPedido(StatusPedidoEnum.ABERTO.getId());
        ItemPedido itemPedido = easyRandom.nextObject(ItemPedido.class);
        itemPedido.getProduto().setAtivo(true);
        Produto produto = easyRandom.nextObject(Produto.class);
        produto.setTipoDemanda(TipoDemandaEnum.PRODUTO.getId());
        produto.setValorUnitario(BigDecimal.valueOf(100));
        when(pedidoMapper.mapper(any())).thenReturn(pedido);
        when(itemMapper.mapperSemPedido(any())).thenReturn(itemPedido);
        when(produtoRepository.findById(any())).thenReturn(Optional.of(produto));
        when(pedidoRepository.save(any())).thenReturn(pedido);
        when(itemRepository.save(any())).thenReturn(pedido);
        when(itemRepository.saveAll(any())).thenReturn(Arrays.asList(itemPedido));
        when(pedidoMapper.mapper(any(), (List<ItemPedido>) any())).thenReturn(new PedidoSaidaDto());
        PedidoSaidaDto result =
                pedidoService.salvar(pedidoDto);
        verify(pedidoMapper).mapper(any());
        verify(itemMapper).mapperSemPedido(any());
        verify(produtoRepository).findById(any());
        verify(pedidoRepository).save(any());
        verify(pedidoMapper).mapper(any(), (List<ItemPedido>) any());
        Assertions.assertEquals(new PedidoSaidaDto(), result);
    }


    @Test
    void testAtualizar_SemDesconto() {

        Produto produto = easyRandom.nextObject(Produto.class);
        produto.setTipoDemanda(TipoDemandaEnum.SERVICO.getId());
        produto.setValorUnitario(BigDecimal.valueOf(50));
        produto.setAtivo(true);

        ItemPedido itemPedido = easyRandom.nextObject(ItemPedido.class);
        itemPedido.setProduto(produto);

        Pedido pedido = easyRandom.nextObject(Pedido.class);
        pedido.setItens(Arrays.asList(itemPedido));

        when(pedidoRepository.findById(any())).thenReturn(Optional.of(pedido));
        doNothing().when(pedidoMapper).mapper(any(), (PedidoDto) any());
        when(produtoRepository.findById(any())).thenReturn(Optional.of(produto));
        when(pedidoRepository.save(any())).thenReturn(pedido);
        when(pedidoMapper.mapper(any(), (List<ItemPedido>) any())).thenReturn(new PedidoSaidaDto());

        PedidoSaidaDto result =
                pedidoService.atualizar(uuidStr, easyRandom.nextObject(PedidoDto.class));
        verify(pedidoRepository).findById(any());
        verify(pedidoMapper).mapper(any(), (PedidoDto) any());
        verify(produtoRepository).findById(any());
        verify(pedidoRepository).save(any());
        verify(pedidoMapper).mapper(any(), (List<ItemPedido>) any());
        Assertions.assertEquals(new PedidoSaidaDto(), result);
    }

    @Test
    void testRemover() {
        when(pedidoRepository.findById(any())).thenReturn(Optional.of(new Pedido()));
        doNothing().when(pedidoRepository).deleteById(any());
        RespostaDto result =
                pedidoService.remover(uuidStr);
        verify(pedidoRepository).findById(any());
        verify(pedidoRepository).deleteById(any());
        Assertions.assertEquals(String.valueOf(HttpStatus.ACCEPTED.value()), result.getStatus());
        Assertions.assertEquals(String.format("Pedido uuid:'%s' removido com sucesso", uuidStr), result.getMessage());
    }

}