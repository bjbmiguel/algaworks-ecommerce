package com.algaworks.ecommerce.consultasnativa;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Cliente;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;

import java.math.BigDecimal;
import java.util.List;

public class StoredProceduresTest extends EntityManagerTest {

    @Test
    public void atualizarPrecoProdutoExercicio(){

        BigDecimal precoAjustado = null;

        //Definimos a st
        StoredProcedureQuery storedProcedureQuery = entityManager
                .createStoredProcedureQuery("ajustar_preco_produto");

        storedProcedureQuery.registerStoredProcedureParameter(
                "produto_id", Integer.class, ParameterMode.IN);

        storedProcedureQuery.registerStoredProcedureParameter(
                "percentual_ajuste", BigDecimal.class, ParameterMode.IN);

        storedProcedureQuery.registerStoredProcedureParameter(
                "preco_ajustado", String.class, ParameterMode.OUT);

        storedProcedureQuery.setParameter("produto_id", 1);
        storedProcedureQuery.setParameter("percentual_ajuste", new BigDecimal("0.1"));

         precoAjustado = new BigDecimal((String)storedProcedureQuery.getOutputParameterValue("preco_ajustado"));

        Assertions.assertEquals(new BigDecimal("878.9"), precoAjustado);
    }

    @Test
    public void chamarNamedStoredProcedure() {
        StoredProcedureQuery storedProcedureQuery = entityManager
                .createNamedStoredProcedureQuery("compraram_acima_media");

        storedProcedureQuery.setParameter("ano", 2020);

        List<Cliente> lista = storedProcedureQuery.getResultList();

        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    public void receberListaDaProcedure() {
        StoredProcedureQuery storedProcedureQuery = entityManager
                .createStoredProcedureQuery("compraram_acima_media", Cliente.class);

        storedProcedureQuery.registerStoredProcedureParameter(
                "ano", Integer.class, ParameterMode.IN);

        storedProcedureQuery.setParameter("ano", 2020);

        List<Cliente> lista = storedProcedureQuery.getResultList();

        Assertions.assertFalse(lista.isEmpty());
    }

    @Test
    public void usarParametrosInEOut() {

        //Definimos a st
        StoredProcedureQuery storedProcedureQuery = entityManager
                .createStoredProcedureQuery("buscar_nome_produto");

        //Registramos o param de entrada.. IN
        storedProcedureQuery.registerStoredProcedureParameter(
                "produto_id", Integer.class, ParameterMode.IN);

        //Registramos o param.. OUT
        storedProcedureQuery.registerStoredProcedureParameter(
                "produto_nome", String.class, ParameterMode.OUT);

        //Passamos o parâmetro...
        storedProcedureQuery.setParameter("produto_id", 1);

        //Recebemos o output...
        String nome = (String) storedProcedureQuery
                .getOutputParameterValue("produto_nome");

        Assertions.assertEquals("Kindle", nome);
    }
}