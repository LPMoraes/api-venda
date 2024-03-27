package com.senior.apivenda.repository;

import com.senior.apivenda.model.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, UUID> {
    @Query(
        nativeQuery = true,
        value = " select distinct p.* " +
                " from pedido p " +
                " join item_pedido ip on p.id = ip.id_pedido " +
                " order by p.dt_inclusao "
    )
    Page<Pedido> findAllAsc(Pageable pageable);
}
