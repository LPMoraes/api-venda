package com.senior.apivenda.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "produto")
@Data
public class Produto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @NotEmpty
    @Size(min = 5, max = 100)
    @Column(name="nome", nullable = false, length = 100)
    private String nome;

    @NotNull
    @NotEmpty
    @Size(min = 5, max = 300)
    @Column(name="descricao" , nullable = false, length = 300)
    private String descricao;

    @NotNull
    @Column(name="vl_unitario", nullable = false)
    private BigDecimal valorUnitario;

    @NotNull
    @Column(name="quantidade", columnDefinition = "SMALLINT", nullable = false)
    private Integer quantidade;

    @NotNull
    @Column(name="tipo_demanda", columnDefinition = "SMALLINT", nullable = false)
    private Integer tipoDemanda;

    @Column(name="dt_alteracao", columnDefinition = "TIMESTAMP")
    private LocalDateTime dataAlteracao;

    @Column(name="dt_inclusao", columnDefinition = "TIMESTAMP")
    private LocalDateTime dataInclusao;

    @Column(name = "ativo", columnDefinition = "BOOLEAN DEFAULT 'true'", nullable = false)
    private Boolean ativo;
}
