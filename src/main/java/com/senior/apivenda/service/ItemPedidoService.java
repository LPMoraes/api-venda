package com.senior.apivenda.service;

import com.senior.apivenda.advice.exception.ItemNaoEncontradoException;
import com.senior.apivenda.model.ItemPedido;
import com.senior.apivenda.model.dto.ItemPedidoDto;
import com.senior.apivenda.model.dto.ItemPedidoSaidaDto;
import com.senior.apivenda.model.dto.RespostaDto;
import com.senior.apivenda.model.mapper.ItemPedidoMapper;
import com.senior.apivenda.repository.ItemPedidoRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class ItemPedidoService {

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private ItemPedidoMapper itemPedidoMapper;

    public Page<ItemPedidoSaidaDto> buscarTodos(Pageable pageable){
        log.info("[00] ItemPedidoService :: buscarTodos ");
        List<ItemPedido> itens = itemPedidoRepository.findAll();
        int end = Math.min(((int) pageable.getOffset() + pageable.getPageSize()), itens.size());
        return new PageImpl<>(
                itens.subList((int) pageable.getOffset(), end)
                    .stream()
                    .map(itemPedidoMapper::mapperSemItemFilho)
                    .collect(Collectors.toList()),
                 pageable,
                 itens.size()
        );
    }

    public ItemPedidoSaidaDto buscarPorId(String uuid){
        log.info("[00] ItemPedidoService :: buscarPorId :: uuid: {} ", uuid);
        return itemPedidoMapper.mapperSemItemFilho(itemPorId(uuid));
    }

    public ItemPedidoSaidaDto salvar(ItemPedidoDto entradaDto){
        log.info("[00] ItemPedidoService :: salvar :: entradaDto: {} ", entradaDto);
        ItemPedido itemPedido = itemPedidoRepository.save(itemPedidoMapper.mapper(entradaDto));
        itemPedido.setProduto(produtoService
                .produtoPorId(String.valueOf(itemPedido.getProduto().getId())));
        itemPedido.setPedido(pedidoService
                .pedidoPorId(String.valueOf(itemPedido.getPedido().getId())));
        log.info("[01] ItemPedidoService :: salvar :: itemPedido: {} ", itemPedido);
        return itemPedidoMapper.mapperSemItemFilho(itemPedido);
    }


    public ItemPedidoSaidaDto atualizar(String uuid, ItemPedidoDto entradaDto){
        log.info("[00] ItemPedidoService :: atualizar :: uuid: {} :: entradaDto: {} ", uuid, entradaDto);
        ItemPedido itemPedido = itemPedidoMapper.mapper(itemPorId(uuid), entradaDto);
        itemPedidoRepository.save(itemPedido);
        itemPedido.setProduto(produtoService
                .produtoPorId(String.valueOf(itemPedido.getProduto().getId())));
        itemPedido.setPedido(pedidoService
                .pedidoPorId(String.valueOf(itemPedido.getPedido().getId())));
        return itemPedidoMapper.mapperSemItemFilho(itemPedido);
    }

    public RespostaDto remover(String uuid){
        log.info("[00] ItemPedidoService :: remover :: uuid: {} ", uuid);
        itemPorId(uuid);
        itemPedidoRepository.deleteById(UUID.fromString(uuid));
        return RespostaDto.sucess(String.format("ItemPedido uuid:'%s' removido com sucesso", uuid),
                HttpStatus.ACCEPTED);
    }

    public ItemPedido itemPorId(String uuid){
        return  itemPedidoRepository.findById(UUID.fromString(uuid))
                .orElseThrow(() -> new ItemNaoEncontradoException(String
                        .format("ItemPedido uuid:'%s' não encontrado ", uuid)));
    }
}
