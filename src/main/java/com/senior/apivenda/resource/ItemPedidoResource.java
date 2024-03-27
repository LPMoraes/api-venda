package com.senior.apivenda.resource;

import com.senior.apivenda.model.dto.ItemPedidoDto;
import com.senior.apivenda.model.dto.ItemPedidoSaidaDto;
import com.senior.apivenda.model.dto.RespostaDto;
import com.senior.apivenda.service.ItemPedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/itens")
public class ItemPedidoResource {

    @Autowired
    private ItemPedidoService ItemPedidoService;

    @Operation(
        summary = "Retorna a lista de itens de pedido",
        responses = {
                @ApiResponse(responseCode = "200", description = "Retorna todos os itens de pedido"),
                @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content(schema = @Schema(implementation = RespostaDto.class))),
                @ApiResponse(responseCode = "500", description = "Retorna exceção não mapeada", content = @Content(schema = @Schema(implementation = RespostaDto.class)))
    })
    @GetMapping
    public ResponseEntity<Page<ItemPedidoSaidaDto>> buscar(@RequestParam(defaultValue = "0", required = false) final int page,
                                                           @RequestParam(defaultValue = "10", required = false) final int size){
        return new ResponseEntity<>(ItemPedidoService.buscarTodos(PageRequest.of(page, size)),
                HttpStatus.OK);
    }


    @Operation(
        summary = "Retorna item do pedido pelo 'UUID'",
        responses = {
            @ApiResponse(responseCode = "200", description = "Retorna item do pedido"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content(schema = @Schema(implementation = RespostaDto.class))),
            @ApiResponse(responseCode = "404", description = "Item do pedido não encontrado", content = @Content(schema = @Schema(implementation = RespostaDto.class))),
            @ApiResponse(responseCode = "500", description = "Retorna exceção não mapeada", content = @Content(schema = @Schema(implementation = RespostaDto.class)))
    })
    @GetMapping(value = "/{uuid}")
    public ResponseEntity<ItemPedidoSaidaDto> buscarPorId(@PathVariable String uuid){
        return new ResponseEntity<>(ItemPedidoService.buscarPorId(uuid), HttpStatus.OK);
    }


    @Operation(
        summary = "Criar novo item de pedido",
        responses = {
            @ApiResponse(responseCode = "200", description = "Cadastrar item de pedido"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content(schema = @Schema(implementation = RespostaDto.class))),
            @ApiResponse(responseCode = "404", description = "Caso o pedido ou produto inseridos no item de pedido não estejam previamente cadastrado",
                    content = @Content(schema = @Schema(implementation = RespostaDto.class))),
            @ApiResponse(responseCode = "500", description = "Retorna exceção não mapeada", content = @Content(schema = @Schema(implementation = RespostaDto.class)))
    })
    @PostMapping
    public ResponseEntity<ItemPedidoSaidaDto> criar(@Valid @RequestBody ItemPedidoDto entrada){
        return new ResponseEntity<>(ItemPedidoService.salvar(entrada), HttpStatus.CREATED);
    }


    @Operation(
        summary = "Atualizar dados do item do pedido",
        responses = {
            @ApiResponse(responseCode = "200", description = "Atualizar item do pedido"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content(schema = @Schema(implementation = RespostaDto.class))),
            @ApiResponse(responseCode = "404", description = "[1] Caso item do pedido não for encontrado ", content = @Content(schema = @Schema(implementation = RespostaDto.class))),
@ApiResponse(responseCode = "500", description = "Retorna exceção não mapeada", content = @Content(schema = @Schema(implementation = RespostaDto.class)))
    })
    @PutMapping(value = "/{uuid}")
    public ResponseEntity<ItemPedidoSaidaDto> atualizar(@PathVariable String uuid, @Valid @RequestBody ItemPedidoDto entrada){
        return new ResponseEntity<>(ItemPedidoService.atualizar(uuid, entrada), HttpStatus.OK);
    }

    @Operation(
        summary = "Remover item de pedido",
        responses = {
            @ApiResponse(responseCode = "202", description = "Item removido"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content(schema = @Schema(implementation = RespostaDto.class))),
            @ApiResponse(responseCode = "404", description = "Item de pedido não encontrado",
                    content = @Content(schema = @Schema(implementation = RespostaDto.class))),
            @ApiResponse(responseCode = "500", description = "Retorna exceção não mapeada", content = @Content(schema = @Schema(implementation = RespostaDto.class)))
    })
    @DeleteMapping(value = "/{uuid}")
    public ResponseEntity<RespostaDto> remover(@PathVariable String uuid){
        return new ResponseEntity<>(ItemPedidoService.remover(uuid), HttpStatus.ACCEPTED);
    }
}
