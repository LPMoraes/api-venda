package com.senior.apivenda.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "pedido")
@Data
public class Pedido implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Column(name="forma_pagamento", columnDefinition = "SMALLINT", nullable = false)
    private Integer formaPagamento;

    @NotNull
    @Column(name="status_pedido", columnDefinition = "SMALLINT", nullable = false)
    private Integer statusPedido;

    @NotNull
    @Column(name="vl_bruto", columnDefinition = "NUMERIC(38,2)", nullable = false)
    private BigDecimal valorBruto;

    @NotNull
    @Column(name="vl_desconto", columnDefinition = "NUMERIC(38,2)", nullable = false)
    private BigDecimal valorDesconto;

    @NotNull
    @Column(name="vl_liquido", columnDefinition = "NUMERIC(38,2)", nullable = false)
    private BigDecimal valorLiquido;

    @NotNull
    @Column(name="percentual_desconto", columnDefinition = "NUMERIC(10,2) ", nullable = false)
    private Double percentualDesconto;

    @Column(name="dt_alteracao", columnDefinition = "TIMESTAMP")
    private LocalDateTime dataAlteracao;

//    @CurrentTimestamp
    @Column(name="dt_inclusao", columnDefinition = "TIMESTAMP")
    private LocalDateTime dataInclusao;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "pedido")
    private List<ItemPedido> itens;
}
