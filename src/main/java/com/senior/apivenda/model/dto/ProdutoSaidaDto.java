package com.senior.apivenda.model.dto;

import com.senior.apivenda.model.enums.TipoDemandaEnum;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ProdutoSaidaDto implements Serializable {
    private UUID id;
    private String nome;
    private String descricao;
    private BigDecimal valor;
    private Integer quantidade;
    private TipoDemandaEnum tipoDemanda;
    private LocalDateTime dataAlteracao;
    private LocalDateTime dataInclusao;
    private Boolean ativo;
}
