package com.senior.apivenda.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "item_pedido")
@Data
public class ItemPedido implements Serializable {

    public ItemPedido(){
        pedido = new Pedido();
        produto =  new Produto();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_pedido", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk01_item_pedido_x_pedido"))
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "id_produto", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk02_item_pedido_x_produto"))
    private Produto produto;

    @Column(name="dt_alteracao", columnDefinition = "TIMESTAMP")
    private LocalDateTime dataAlteracao;

//    @CurrentTimestamp
    @Column(name="dt_inclusao", columnDefinition = "TIMESTAMP")
    private LocalDateTime dataInclusao;
}
