package com.algaworks.ecommerce.cache;

import com.algaworks.ecommerce.model.Pedido;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.persistence.*;
import java.util.HashMap;
import java.util.Map;

public class CacheTest {

    protected static EntityManagerFactory entityManagerFactory;

    @BeforeAll
    public static void setUpBeforeClass() {
        entityManagerFactory = Persistence
                .createEntityManagerFactory("Ecommerce-PU");
    }

    @AfterAll
    public static void tearDownAfterClass() {
        entityManagerFactory.close();
    }

    private static void esperar(int segundos) {
        try {
            Thread.sleep(segundos * 1000);
        } catch (InterruptedException e) {}
    }

    private static void log(Object obj) {
        System.out.println("[LOG " + System.currentTimeMillis() + "] " + obj);
    }

    @Test
    public void ehcache() {
        Cache cache = entityManagerFactory.getCache();

        EntityManager entityManager1 = entityManagerFactory.createEntityManager();
        EntityManager entityManager2 = entityManagerFactory.createEntityManager();

        log("Buscando e incluindo no cache...");
        entityManager1
                .createQuery("select p from Pedido p", Pedido.class)
                .getResultList();
        log("---");

        esperar(1);
        Assertions.assertTrue(cache.contains(Pedido.class, 2));
        entityManager2.find(Pedido.class, 2);

        esperar(3);
        Assertions.assertFalse(cache.contains(Pedido.class, 2));

        entityManager1.close();
        entityManager2.close();
    }

    @Test
    public void controlarCacheDinamicamente() {
        // jakarta.persistence.cache.retrieveMode CacheRetrieveMode
        // jakarta.persistence.cache.storeMode CacheStoreMode

        Cache cache = entityManagerFactory.getCache();

        System.out.println("Buscando todos os pedidos..........................");
        EntityManager entityManager1 = entityManagerFactory.createEntityManager();
        entityManager1.setProperty("jakarta.persistence.cache.storeMode", CacheStoreMode.BYPASS);
        entityManager1
                .createQuery("select p from Pedido p", Pedido.class)
                .setHint("jakarta.persistence.cache.storeMode", CacheStoreMode.USE)
                .getResultList();
        entityManager1.close();

        System.out.println("Buscando o pedido de ID igual a 2..................");
        EntityManager entityManager2 = entityManagerFactory.createEntityManager();
        Map<String, Object> propriedades = new HashMap<>();
//        propriedades.put("jakarta.persistence.cache.storeMode", CacheStoreMode.BYPASS);
//        propriedades.put("jakarta.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
        entityManager2.find(Pedido.class, 2, propriedades);
        entityManager2.close();

        System.out.println("Buscando todos os pedidos (de novo)..........................");
        EntityManager entityManager3 = entityManagerFactory.createEntityManager();
        entityManager3
                .createQuery("select p from Pedido p", Pedido.class)
//                .setHint("jakarta.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS)
                .getResultList();
        entityManager3.close();
    }

    @Test
    public void analisarOpcoesCache() {
        Cache cache = entityManagerFactory.getCache();

        EntityManager entityManager1 = entityManagerFactory.createEntityManager();

        System.out.println("Buscando a partir da instância 1:");
        entityManager1
                .createQuery("select p from Pedido p", Pedido.class)
                .getResultList();

        Assertions.assertTrue(cache.contains(Pedido.class, 1));

        entityManager1.close();
    }

    @Test
    public void verificarSeEstaNoCache() {
        Cache cache = entityManagerFactory.getCache();

        EntityManager entityManager1 = entityManagerFactory.createEntityManager();

        System.out.println("Buscando a partir da instância 1:");
        entityManager1
                .createQuery("select p from Pedido p", Pedido.class)
                .getResultList();

        Assertions.assertTrue(cache.contains(Pedido.class, 1));
        Assertions.assertTrue(cache.contains(Pedido.class, 2));

        entityManager1.close();
    }

    @Test
    public void removerDoCache() {
        Cache cache = entityManagerFactory.getCache();

        EntityManager entityManager1 = entityManagerFactory.createEntityManager();
        EntityManager entityManager2 = entityManagerFactory.createEntityManager();

        System.out.println("Buscando a partir da instância 1:");
        entityManager1
                .createQuery("select p from Pedido p", Pedido.class)
                .getResultList();

        System.out.println("Removendo do cache");
        cache.evictAll();
//        cache.evict(Pedido.class);
//        cache.evict(Pedido.class, 1);

        System.out.println("Buscando a partir da instância 2:");
        entityManager2.find(Pedido.class, 1);
        entityManager2.find(Pedido.class, 2);

        entityManager1.close();
        entityManager2.close();
    }

    @Test
    public void adicionarPedidosNoCache() {
        EntityManager entityManager1 = entityManagerFactory.createEntityManager();
        EntityManager entityManager2 = entityManagerFactory.createEntityManager();

        System.out.println("Buscando a partir da instância 1:");
        entityManager1
                .createQuery("select p from Pedido p", Pedido.class)
                .getResultList();

        System.out.println("Buscando a partir da instância 2:");
        entityManager2.find(Pedido.class, 1);

        entityManager1.close();
        entityManager2.close();
    }

    @Test
    public void buscarDoCache() {
        EntityManager entityManager1 = entityManagerFactory.createEntityManager();
        EntityManager entityManager2 = entityManagerFactory.createEntityManager();

        System.out.println("Buscando a partir da instância 1:");
        entityManager1.find(Pedido.class, 1);

        System.out.println("Buscando a partir da instância 2:");
        entityManager2.find(Pedido.class, 1);

        entityManager1.close();
        entityManager2.close();
    }
}