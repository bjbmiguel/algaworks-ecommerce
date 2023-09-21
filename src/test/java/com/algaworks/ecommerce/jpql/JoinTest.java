package com.algaworks.ecommerce.jpql;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.ItemPedido;
import com.algaworks.ecommerce.model.Pedido;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jakarta.persistence.TypedQuery;

import java.util.List;

public class JoinTest extends EntityManagerTest {

    @Test
    public void buscarPedidoComProdutoEspecifico() {

        String jpql = "select p from Pedido p " +
                "join fetch p.itens i " +
                "join fetch p.cliente cli " +
                "left join fetch p.notaFiscal " +
                "left join fetch p.pagamento " +
                "left join fetch i.produto prod " +
                "left join fetch prod.estoque " +
                "where i.id.produtoId = 1";

        //String jpql = " select i from ItemPedido i join fetch i.pedido p join fetch i.produto pr where pr.id = 1 ";

        TypedQuery<ItemPedido> typedQuery = entityManager.createQuery(jpql, ItemPedido.class);

        List<ItemPedido> lista = typedQuery.getResultList();
        Assertions.assertEquals(4, lista.size());
    }

    @Test
    public void usarJoinFetch() {
        String jpql_fetch = "select p from Pedido p "
                + " left join fetch p.pagamento "
                + " join fetch p.cliente "
                + " left join fetch p.notaFiscal ";

        // String jpql = "select p from Pedido p join fetch p.pagamento";
        TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql_fetch, Pedido.class);

        List<Pedido> lista = typedQuery.getResultList();
        //System.out.println("lista.size() = " + lista.size());
        Assertions.assertNotNull(lista.get(0).getItens());
    }

    @Test
    public void fazerLeftJoin() {
        String jpql = "select p from Pedido p left join p.pagamento pag on pag.status = 'PROCESSANDO'";

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);

        List<Object[]> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    public void fazerJoin() {
        String jpql = "select p from Pedido p join p.pagamento pag";

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);

        List<Object[]> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());
    }
}