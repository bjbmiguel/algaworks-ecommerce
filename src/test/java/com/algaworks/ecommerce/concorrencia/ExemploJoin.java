package com.algaworks.ecommerce.concorrencia;

public class ExemploJoin {
    public static void main(String[] args) {// Thread principal

        Thread thread1 = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                System.out.println("Thread 1: " + i);
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                System.out.println("Thread 2: " + i);
            }
        });

        thread1.start();
        thread2.start();

        try {

            // A thread principal (a thread que executa o método main) espera até que thread1 termine
            thread1.join();
            // A thread2 espera até que thread1 termine
            thread2.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Thread principal continua após thread1
        System.out.println("Thread principal: Fim");
    }
}
