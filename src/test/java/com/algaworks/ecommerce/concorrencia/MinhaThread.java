package com.algaworks.ecommerce.concorrencia;

public class MinhaThread extends Thread {
    public void run() {
        // Código que a thread executará
        for (int i = 1; i <= 10; i++) {
            System.out.println("Thread: " + i);
        }
    }

    public static void main(String[] args) {
        MinhaThread minhaThread = new MinhaThread();
        minhaThread.start();
    }
}
