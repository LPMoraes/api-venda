package com.senior.apivenda.resource;

import com.senior.apivenda.model.Pedido;
import com.senior.apivenda.model.dto.PedidoDto;
import com.senior.apivenda.model.dto.PedidoSaidaDto;
import com.senior.apivenda.model.dto.RespostaDto;
import com.senior.apivenda.service.PedidoService;
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
@RequestMapping(value = "/pedidos")
public class PedidoResource {

    @Autowired
    private PedidoService pedidoService;

    @Operation(
        summary = "Retorna a lista de pedidos",
        responses = {
            @ApiResponse(responseCode = "200", description = "Retorna todos os pedidos"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content(schema = @Schema(implementation = RespostaDto.class))),
            @ApiResponse(responseCode = "500", description = "Retorna exceção não mapeada", content = @Content(schema = @Schema(implementation = RespostaDto.class)))
    })
    @GetMapping
    public ResponseEntity<Page<Pedido>> buscar(@RequestParam(defaultValue = "0", required = false) final int page,
                                               @RequestParam(defaultValue = "10", required = false) final int size){
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(pedidoService.buscarTodos(pageable),
                HttpStatus.OK);
    }

    @Operation(
        summary = "Retorna pedido pelo 'UUID'",
        responses = {
            @ApiResponse(responseCode = "200", description = "Retorna pedido"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content(schema = @Schema(implementation = RespostaDto.class))),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content(schema = @Schema(implementation = RespostaDto.class))),
            @ApiResponse(responseCode = "500", description = "Retorna exceção não mapeada", content = @Content(schema = @Schema(implementation = RespostaDto.class)))
    })
    @GetMapping(value = "/{uuid}")
    public ResponseEntity<PedidoSaidaDto> buscarPorId(@PathVariable String uuid){
        return new ResponseEntity<>(pedidoService.buscarPorId(uuid), HttpStatus.OK);
    }

    @Operation(
        summary = "Criar novo pedido",
        responses = {
            @ApiResponse(responseCode = "200", description = "Cadastrar pedido", content = @Content(schema = @Schema(implementation = PedidoSaidaDto.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content(schema = @Schema(implementation = RespostaDto.class))),
            @ApiResponse(responseCode = "404", description = "[1] Pedido possua 'FormaPagamento' ou 'StatusPedido não cadastrado; " +
                    "[2] Caso produto enviado possua 'TipoDemanda' não cadastrado; [3] Caso  produto inserido não exista ou esteja desativado"
                    , content = @Content(schema = @Schema(implementation = RespostaDto.class))),
            @ApiResponse(responseCode = "500", description = "Retorna exceção não mapeada", content = @Content(schema = @Schema(implementation = RespostaDto.class)))
    })
    @PostMapping
    public ResponseEntity<PedidoSaidaDto> criar(@Valid @RequestBody PedidoDto entrada){
        return new ResponseEntity<>(pedidoService.salvar(entrada), HttpStatus.CREATED);
    }


    @Operation(
        summary = "Atualizar dados do pedido",
        responses = {
            @ApiResponse(responseCode = "200", description = "Atualizar pedido"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content(schema = @Schema(implementation = RespostaDto.class))),
            @ApiResponse(responseCode = "404", description = "[1] Caso pedido não for encontrado, [2] Pedido possua 'FormaPagamento' ou 'StatusPedido não cadastrado " +
                    "[3] Caso  produto inserido não exista ou esteja desativado ", content = @Content(schema = @Schema(implementation = RespostaDto.class))),
            @ApiResponse(responseCode = "500", description = "Retorna exceção não mapeada", content = @Content(schema = @Schema(implementation = RespostaDto.class)))
    })
    @PutMapping(value = "/{uuid}")
    public ResponseEntity<PedidoSaidaDto> atualizar(@PathVariable String uuid, @Valid @RequestBody PedidoDto entrada){
        return new ResponseEntity<>(pedidoService.atualizar(uuid, entrada), HttpStatus.OK);
    }

    @Operation(
        summary = "Remover pedido",
        responses = {
            @ApiResponse(responseCode = "202", description = "Pedido removido"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content(schema = @Schema(implementation = RespostaDto.class))),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado",
                    content = @Content(schema = @Schema(implementation = RespostaDto.class))),
            @ApiResponse(responseCode = "500", description = "Retorna exceção não mapeada", content = @Content(schema = @Schema(implementation = RespostaDto.class)))
    })
    @DeleteMapping(value = "/{uuid}")
    public ResponseEntity<RespostaDto> remover(@PathVariable String uuid){
        return new ResponseEntity<>(pedidoService.remover(uuid), HttpStatus.ACCEPTED);
    }
}