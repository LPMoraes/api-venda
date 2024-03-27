package com.senior.apivenda.service;

import com.senior.apivenda.advice.exception.ProdutoNaoEncontradoException;
import com.senior.apivenda.advice.exception.ProdutoRelecionadoAPedidoException;
import com.senior.apivenda.model.Produto;
import com.senior.apivenda.model.dto.ProdutoDto;
import com.senior.apivenda.model.dto.ProdutoSaidaDto;
import com.senior.apivenda.model.dto.RespostaDto;
import com.senior.apivenda.model.mapper.ProdutoMapper;
import com.senior.apivenda.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@Transactional
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ProdutoMapper produtoMapper;

    @Autowired
    private RespostaService respostaService;


    public Page<Produto> buscarTodos(Pageable pageable){
        log.info("[00] ProdutoService :: buscarTodos ");
        return produtoRepository.findAll(pageable);
    }

    public ProdutoSaidaDto buscarPorId(String uuid){
        log.info("[00] ProdutoService :: buscarPorId :: uuid: {} ", uuid);
        return produtoMapper.mapper(produtoPorId(uuid));
    }

    public Page<Produto> buscarTodosAtivos(Pageable pageable){
        log.info("[00] ProdutoService :: buscarTodosAtivos ");
        return produtoRepository.findAllIsAtivo(pageable);
    }

    public ProdutoSaidaDto salvar(ProdutoDto entradaDto){
        log.info("[00] ProdutoService :: salvar :: entradaDto: {} ", entradaDto);
        Produto produto = produtoRepository.save(produtoMapper.mapper(entradaDto));
        log.info("[01] ProdutoService :: salvar :: produto: {} ", produto);
        return produtoMapper.mapper(produto);
    }

    public ProdutoSaidaDto atualizar(String uuid, ProdutoDto entradaDto){
        log.info("[00] ProdutoService :: produtoPorId :: uuid: {} ", uuid);
        Produto produto =  produtoPorId(uuid);
        produtoMapper.mapper(produto, entradaDto);
        log.info("[01] ProdutoService :: atualizar :: produto: {} ", produto);
        return produtoMapper.mapper(produtoRepository.save(produto));
    }


    public RespostaDto remover(String uuid){
        log.info("[00] ProdutoService :: remover :: uuid: {} ", uuid);
        validarProduto(uuid);
        produtoRepository.deleteById(UUID.fromString(uuid));
        return RespostaDto.sucess(String.format("Produto uuid:'%s' removido com sucesso", uuid),
                HttpStatus.ACCEPTED);
    }

    private void validarProduto(String uuid){
        produtoPorId(uuid);
        produtoRepository.isPedido(UUID.fromString(uuid)).ifPresent(p -> {
            throw new ProdutoRelecionadoAPedidoException(String
                    .format("Não será possível remover Produto uuid:'%s' pois o mesmo está relacionado a um pedido", uuid));
        });
    }

    public Produto produtoPorId(String uuid){
        return  produtoRepository.findById(UUID.fromString(uuid))
                .orElseThrow(() -> new ProdutoNaoEncontradoException(String
                        .format("Produto uuid:'%s' não encontrado ", uuid)));
    }

}
