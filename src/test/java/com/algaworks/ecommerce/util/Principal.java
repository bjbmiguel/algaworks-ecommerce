package com.algaworks.ecommerce.util;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.internal.util.StringHelper;

import java.util.ArrayList;
import java.util.List;

public class Principal {

    @Setter
    @Getter
    static class Aluno {

        private int id;
        private String nome;

    }

    public static void main(String[] args) {
         int pk = Integer.parseInt(args[0]);
        String nome = args[1];
        cadastrar(pk, nome);
    }

    private static Aluno cadastrar(int pk, String nome){
        var aluno = new Aluno();
        aluno.setId(pk);
        aluno.setNome(nome + pk);
        System.out.println("Aluno salvo com sucesso");

        return aluno;
    }


//    public static void main(String[] args) {
//        List<String> stringList = new ArrayList<>();
//
//        for (int i = 0; i < 1000000; i++) {
//            String data = "Data " + i;
//            stringList.add(data);
//        }
//
//        stringList = new ArrayList<>();
//        for (int i = 0; i < 1000000; i++) {
//            String data = "Data 2 " + i;
//            stringList.add(data);
//        }
//
//        // Omitimos a remoção dos elementos da lista intencionalmente
//
//        System.out.println("Programa concluído.");
//    }
}
