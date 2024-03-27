package com.senior.apivenda.resource;

import com.senior.apivenda.model.Produto;
import com.senior.apivenda.model.dto.ProdutoDto;
import com.senior.apivenda.model.dto.ProdutoSaidaDto;
import com.senior.apivenda.model.dto.RespostaDto;
import com.senior.apivenda.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/produtos")
public class ProdutoResource {

    @Autowired
    private ProdutoService produtoService;

    @Operation(
         summary = "Retorna a lista de produtos",
         responses = {
             @ApiResponse(responseCode = "200", description = "Retorna todos os produtos"),
             @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content(schema = @Schema(implementation = RespostaDto.class))),
             @ApiResponse(responseCode = "500", description = "Retorna exceção não mapeada", content = @Content(schema = @Schema(implementation = RespostaDto.class)))
    })
    @GetMapping
    public ResponseEntity<Page<Produto>> buscar(@RequestParam(defaultValue = "0", required = false) final int page,
                                                @RequestParam(defaultValue = "10", required = false) final int size){
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(produtoService.buscarTodos(pageable), HttpStatus.OK);
    }
    @Operation(
        summary = "Retorna a lista de produtos ativos",
        responses = {
            @ApiResponse(responseCode = "200", description = "Retorna todos os produtos ativos"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content(schema = @Schema(implementation = RespostaDto.class))),
            @ApiResponse(responseCode = "500", description = "Retorna exceção não mapeada", content = @Content(schema = @Schema(implementation = RespostaDto.class)))
    })
    @GetMapping("/ativos")
    public ResponseEntity<Page<Produto>> buscarAtivos(@RequestParam(defaultValue = "0", required = false) final int page,
                                          @RequestParam(defaultValue = "10", required = false) final int size){
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(produtoService.buscarTodosAtivos(pageable), HttpStatus.OK);
    }

    @Operation(
        summary = "Retorna produto pelo 'UUID'",
        responses = {
           @ApiResponse(responseCode = "200", description = "Retorna produto"),
           @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content(schema = @Schema(implementation = RespostaDto.class))),
           @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content(schema = @Schema(implementation = RespostaDto.class))),
           @ApiResponse(responseCode = "500", description = "Retorna exceção não mapeada", content = @Content(schema = @Schema(implementation = RespostaDto.class)))
    })
    @GetMapping(value = "/{uuid}")
    public ResponseEntity<ProdutoSaidaDto> buscarPorId(@PathVariable String uuid){
        return new ResponseEntity<>(produtoService.buscarPorId(uuid), HttpStatus.OK);
    }

    @Operation(
        summary = "Criar novo produto",
        responses = {
           @ApiResponse(responseCode = "200", description = "Cadastrar produto"),
           @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content(schema = @Schema(implementation = RespostaDto.class))),
           @ApiResponse(responseCode = "404", description = "Caso produto enviado possua 'TipoDemanda' não cadastrada", content = @Content(schema = @Schema(implementation = RespostaDto.class))),
           @ApiResponse(responseCode = "500", description = "Retorna exceção não mapeada", content = @Content(schema = @Schema(implementation = RespostaDto.class)))
    })
    @PostMapping
    public ResponseEntity<ProdutoSaidaDto> criar(@Valid @RequestBody ProdutoDto entrada){
        return new ResponseEntity<>(produtoService.salvar(entrada), HttpStatus.CREATED);
    }

    @Operation(
        summary = "Atualizar dados do produto",
        responses = {
            @ApiResponse(responseCode = "200", description = "Atualizar produto"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content(schema = @Schema(implementation = RespostaDto.class))),
            @ApiResponse(responseCode = "404", description = "[1] Caso produto não for encontrado, [2] Caso produto enviado possua 'TipoDemanda' não cadastrada", content = @Content(schema = @Schema(implementation = RespostaDto.class))),
            @ApiResponse(responseCode = "500", description = "Retorna exceção não mapeada", content = @Content(schema = @Schema(implementation = RespostaDto.class)))
    })
    @PutMapping(value = "/{uuid}")
    public ResponseEntity<ProdutoSaidaDto> atualizar(@PathVariable String uuid, @Valid @RequestBody ProdutoDto entrada){
        return new ResponseEntity<>(produtoService.atualizar(uuid, entrada), HttpStatus.OK);
    }

    @Operation(
        summary = "Remover produto",
        responses = {
            @ApiResponse(responseCode = "202", description = "Produto removido"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content(schema = @Schema(implementation = RespostaDto.class))),
            @ApiResponse(responseCode = "404", description = "[1] Caso produto não for encontrado, [2] Caso produto esteja relacionado a um pedido",
                    content = @Content(schema = @Schema(implementation = RespostaDto.class))),
            @ApiResponse(responseCode = "500", description = "Retorna exceção não mapeada", content = @Content(schema = @Schema(implementation = RespostaDto.class)))
    })
    @DeleteMapping(value = "/{uuid}")
    public ResponseEntity<RespostaDto> remover(@PathVariable String uuid){
        return new ResponseEntity<>(produtoService.remover(uuid), HttpStatus.ACCEPTED);
    }
}
