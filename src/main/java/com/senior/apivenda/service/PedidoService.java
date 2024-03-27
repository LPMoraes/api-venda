package com.senior.apivenda.service;

import com.senior.apivenda.advice.exception.PedidoNaoEncontradoException;
import com.senior.apivenda.advice.exception.ProdutoDisabledException;
import com.senior.apivenda.advice.exception.ProdutoNaoEncontradoException;
import com.senior.apivenda.model.ItemPedido;
import com.senior.apivenda.model.Pedido;
import com.senior.apivenda.model.Produto;
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
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@Transactional
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ItemPedidoRepository itemRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PedidoMapper pedidoMapper;

    @Autowired
    private ItemPedidoMapper itemMapper;

    private static Double DOUBLE_HUNDRED = Double.valueOf(100.0);


    public Page<Pedido> buscarTodos(Pageable pageable){
        log.info("[00] PedidoService :: buscarTodos ");
        Page<Pedido> pedidos = pedidoRepository.findAllAsc(pageable);
        return pedidos;
    }

    public PedidoSaidaDto buscarPorId(String uuid){
        log.info("[00] PedidoService :: buscarPorId :: uuid: {} ", uuid);
        Pedido pedido = pedidoPorId(uuid);
        return pedidoMapper.mapper(pedido, pedido.getItens());
    }

    public PedidoSaidaDto salvar(PedidoDto entradaDto){
        Pedido pedido = pedidoMapper.mapper(entradaDto);
        List<ItemPedido> itens = entradaDto.getItens().stream()
                .map(i -> itemMapper.mapperSemPedido(i))
                .map(i -> validarProduto(i))
                .toList();
        calcularValores(pedido, itens);
        pedido.setStatusPedido(StatusPedidoEnum.FECHADO.getId());
        pedidoRepository.save(pedido);
        log.info("[00] PedidoService :: salvar :: pedido: {} ", pedido);
        itens.stream().forEach(i -> i.setPedido(pedido));
        itemRepository.saveAll(itens);
        log.info("[01] PedidoService :: salvar :: itens: {} ", itens);
        return pedidoMapper.mapper(pedido, itens);
    }

    public PedidoSaidaDto atualizar(String uuid, PedidoDto entradaDto){
        log.info("[00] PedidoService :: atualizar :: uuid: {} ", uuid);
        Pedido pedido =  pedidoPorId(uuid);
        pedidoMapper.mapper(pedido, entradaDto);
        List<ItemPedido> itens = pedido.getItens().stream()
                .map(i -> validarProduto(i))
                .toList();
        calcularValores(pedido, itens);
        pedido.setStatusPedido(StatusPedidoEnum.FECHADO.getId());
        pedidoRepository.save(pedido);
        return pedidoMapper.mapper(pedido, itens);
    }


    public RespostaDto remover(String uuid){
        log.info("[00] PedidoService :: remover :: uuid: {} ", uuid);
        pedidoPorId(uuid);
        pedidoRepository.deleteById(UUID.fromString(uuid));
        return RespostaDto.sucess(String.format("Pedido uuid:'%s' removido com sucesso", uuid),
                HttpStatus.ACCEPTED);
    }

    public Pedido pedidoPorId(String uuid){
        return  pedidoRepository.findById(UUID.fromString(uuid))
                .orElseThrow(() -> new PedidoNaoEncontradoException(String
                        .format("Pedido uuid:'%s' não encontrado ", uuid)));
    }

    private ItemPedido validarProduto(ItemPedido itemPedido){
        Produto produto = produtoRepository.findById(itemPedido.getProduto().getId())
                .orElseThrow(() -> new ProdutoNaoEncontradoException(String.format("Produto uuid:'%s' não encontrado",
                        itemPedido.getProduto().getId())));

        if(!produto.getAtivo())
            throw new ProdutoDisabledException(String.format("Não foi possível realizar pedido pois Produto uuid:'%s' encontra-se desativado",
                    itemPedido.getProduto().getId()));

        itemPedido.setProduto(produto);
        return itemPedido;
    }

    private void calcularValores(Pedido pedido, List<ItemPedido> itens){
        BigDecimal valorItensTipoServico = somarTotal(TipoDemandaEnum.SERVICO, itens);
        BigDecimal valorItensTipoProduto = somarTotal(TipoDemandaEnum.PRODUTO, itens);
        pedido.setValorBruto(valorItensTipoServico.add(valorItensTipoProduto));
        pedido.setValorDesconto(BigDecimal.ZERO);
        pedido.setValorLiquido(pedido.getValorBruto());

        if(pedido.getStatusPedido().equals(StatusPedidoEnum.ABERTO.getId())
                && Objects.nonNull(pedido.getPercentualDesconto())
                && pedido.getPercentualDesconto().compareTo(Double.valueOf(0.0)) > 0){
            pedido.setValorLiquido(valorItensTipoServico
                    .add(aplicarDesconto(valorItensTipoProduto, pedido.getPercentualDesconto())));
            pedido.setValorDesconto(pedido.getValorBruto().subtract(pedido.getValorLiquido()));
        }

        log.info("[00] PedidoService :: calcularValores :: valorBruto: {}, valorDesconto: {}, valorLiquido: {}",
                pedido.getValorBruto(), pedido.getValorDesconto(), pedido.getValorLiquido());
    }

    private BigDecimal somarTotal(TipoDemandaEnum tipoDemandaEnum, List<ItemPedido> itens){
        return itens.stream()
                .filter(i -> i.getProduto().getTipoDemanda().equals(tipoDemandaEnum.getId()))
                .map(i -> i.getProduto().getValorUnitario())
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    private BigDecimal aplicarDesconto(BigDecimal valorItensTipoProduto, Double percentualDesconto){
        Double aliquota = (percentualDesconto / DOUBLE_HUNDRED);
        return valorItensTipoProduto.subtract(valorItensTipoProduto.multiply(BigDecimal.valueOf(aliquota))
                        .setScale(2, RoundingMode.HALF_UP));
    }
}
