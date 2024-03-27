package com.senior.apivenda.model.dto;

import com.senior.apivenda.model.enums.TipoDemandaEnum;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ProdutoDto implements Serializable {
    private UUID id;
    private String nome;
    private String descricao;
    private BigDecimal valorUnitario;
    private Integer quantidade;
    private TipoDemandaEnum tipoDemanda;
    private Boolean ativo;
}
