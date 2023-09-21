package com.algaworks.ecommerce.concorrencia;

public class MinhaRunnable implements Runnable {
    public void run() {
        // Código que a thread executará
        for (int i = 1; i <= 10; i++) {
            System.out.println("Thread: " + i);
        }
    }

    public static void main(String[] args) {
        MinhaRunnable minhaRunnable = new MinhaRunnable();
        Thread thread = new Thread(minhaRunnable);
        thread.start();
    }
}
