package com.senior.apivenda.repository;

import com.senior.apivenda.model.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, UUID> {

    @Query(
        nativeQuery = true,
        value = " select p.* from produto p where ativo = true "
    )
    Page<Produto> findAllIsAtivo(Pageable pageable);

    @Query(
        nativeQuery = true,
        value = " select p.* " +
                " from produto p " +
                " join item_pedido i on p.id = i.id_produto " +
                " where i.id_produto = :uuid " +
                " limit 1 "
    )
    Optional<Produto> isPedido(@Param("uuid") UUID uuid);
}
