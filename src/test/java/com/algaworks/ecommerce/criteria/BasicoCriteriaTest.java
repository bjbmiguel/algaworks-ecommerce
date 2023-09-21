package com.algaworks.ecommerce.criteria;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.dto.ProdutoDTO;
import com.algaworks.ecommerce.model.Cliente;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.Pedido_;
import com.algaworks.ecommerce.model.Produto;
import jakarta.persistence.Tuple;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.math.BigDecimal;
import java.util.List;

public class BasicoCriteriaTest extends EntityManagerTest {

    @Test
    public void usarDistinct() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteriaQuery = criteriaBuilder.createQuery(Pedido.class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);
        root.join(Pedido_.itens);

        criteriaQuery.select(root);
        criteriaQuery.distinct(true);

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Pedido> lista = typedQuery.getResultList();

        lista.forEach(p -> System.out.println("ID: " + p.getId()));
    }


    @Test
    public void projetarOResultadoDTO() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProdutoDTO> criteriaQuery = criteriaBuilder.createQuery(ProdutoDTO.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);

        criteriaQuery.select(criteriaBuilder
                .construct(ProdutoDTO.class, root.get("id"), root.get("nome")));

        TypedQuery<ProdutoDTO> typedQuery = entityManager.createQuery(criteriaQuery);
        List<ProdutoDTO> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(dto -> System.out.println("ID: " + dto.getId() + ", Nome: " + dto.getNome()));
    }

    @Test
    public void projetarOResultadoTuple() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createTupleQuery();
        Root<Produto> root = criteriaQuery.from(Produto.class);

        criteriaQuery.select(criteriaBuilder
                .tuple(root.get("id").alias("id"), root.get("nome").alias("nome")));

        TypedQuery<Tuple> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Tuple> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(t -> System.out.println("ID: " + t.get("id") + ", Nome: " + t.get("nome")));
    }

    @Test
    public void projetarOResultado() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Produto> root = criteriaQuery.from(Produto.class);

        criteriaQuery.multiselect(root.get("id"), root.get("nome"));

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Object[]> lista = typedQuery.getResultList();
        Assertions.assertFalse(lista.isEmpty());

        lista.forEach(arr -> System.out.println("ID: " + arr[0] + ", Nome: " + arr[1]));
    }


    @Test
    public void retornarTodosOsProdutosDaBD() {
        //Criamos uma instância de CriteriaBuilder...
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        //Definimos o tipo de retorno da nossa consulta...
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        //O Root representa a entidade raiz da consulta, que é a classe Pedido. Isso define de qual tabela a consulta começará.
        Root<Produto> root = criteriaQuery.from(Produto.class);

        //Aqui, especificamos o que desejamos selecionar, neste caso será a nossa entidade root...
        criteriaQuery.select(root);

        //Criamos uma consulta tipada usando o TypedQuery. A consulta retornará objetos do tipo Produto, conforme especificado na CriteriaQuery.
        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);

        //Aqui, executamos a consulta e esperamos obter umaa lista de produtos.
        List<Produto> produtos = typedQuery.getResultList();
        Assertions.assertFalse(produtos.isEmpty());
    }

    @Test
    public void selecionarUmAtributoParaRetorno2() {
        //Criamos uma instância de CriteriaBuilder...
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        //Definimos o tipo de retorno da nossa consulta...
        CriteriaQuery<Cliente> criteriaQuery = criteriaBuilder.createQuery(Cliente.class);
        //O Root representa a entidade raiz da consulta, que é a classe Pedido. Isso define de qual tabela a consulta começará.
        Root<Pedido> root = criteriaQuery.from(Pedido.class);

        //Aqui, especificamos o que desejamos selecionar, neste caso o atributo "cliente" da entidade Pedido. Isso define a projeção da consulta.
        criteriaQuery.select(root.get("cliente"));
        //Aqui adicionando uma condição à consulta. Neste caso, esatmos  filtrando os pedidos onde o atributo "id" é igual a 1.
        criteriaQuery.where(criteriaBuilder.equal(root.get("id"), 1));

        //Criamos uma consulta tipada usando o TypedQuery. A consulta retornará objetos do tipo Cliente, conforme especificado na CriteriaQuery.
        TypedQuery<Cliente> typedQuery = entityManager.createQuery(criteriaQuery);

        //Aqui, executamos a consulta e esperamos obter um único resultado, que é um objeto Cliente.
        Cliente cliente = typedQuery.getSingleResult(); // Este método espera que um resultado seja retornado, se for zero ou >1 uma exceção será lançada..
        Assertions.assertEquals("Fernando Medeiros", cliente.getNome());
    }

    @Test
    public void selecionarUmAtributoParaRetorno() {
        //Criamos um objecto do tipo CriteriaBuilder...
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        //Definimos o tipo de retorno da nossa consulta usando
        CriteriaQuery<Cliente> criteriaQuery = criteriaBuilder.createQuery(Cliente.class);
        //Definimos o root, ou seja, a tabela principal
        Root<Cliente> root = criteriaQuery.from(Cliente.class);

        //
        criteriaQuery.select(root.get("total"));
        criteriaQuery.where(criteriaBuilder.equal(root.get("id"), 1));

        TypedQuery<Cliente> typedQuery = entityManager.createQuery(criteriaQuery);
        Cliente total = typedQuery.getSingleResult();
        Assertions.assertEquals(new BigDecimal("2398.00"), total);
    }


    @Test
    public void buscarPorIdentificador() {

        //Criamos uma instância de CriteriaBuilder...
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        //Representamos a nossa consulta de maneiroa geral, definindo o tipo de resultado...
        CriteriaQuery<Cliente> criteriaQuery = criteriaBuilder.createQuery(Cliente.class);
        //Definimos a entidade Raiz...
        Root<Cliente> root = criteriaQuery.from(Cliente.class);

        //Executamos um select no root...
        criteriaQuery.select(root); //Este método é opção devido a especificação do root...

        //Adicionamos um where na consulta...
        criteriaQuery.where(criteriaBuilder.equal(root.get("id"), 1));

        TypedQuery<Cliente> typedQuery = entityManager.createQuery(criteriaQuery);

        Cliente cliente = typedQuery.getSingleResult();
        Assertions.assertEquals("Fernando Medeiros", cliente.getNome());
    }

    @Test
    public void buscarPorIdentificadorJPQL() {

        String jpql = "select p from Pedido p where p.id = 1";  // Query JPQL
        TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);  //Perceba que aqui é feito um parse do JPQL para o SQL

        Pedido pedido = typedQuery.getSingleResult(); // Execução da query...
        Assertions.assertNotNull(pedido);
    }
}