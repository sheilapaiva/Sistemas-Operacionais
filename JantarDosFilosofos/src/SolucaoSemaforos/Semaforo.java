package SolucaoSemaforos;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Semaforo {
	static Semaphore mutex = new Semaphore(1);

    static Semaphore[] semaforos = new Semaphore[5]; 
    static Filosofo[] filosofos = new Filosofo[5];

    static int[] estado = new int[5];

    public static void main(String[] args) {
    	// Definição do estado inicial de cada filosofo
        for (int i = 0; i < estado.length; i++) {
            estado[i] = 0;
        }

        // Inicialização dos filosos
        filosofos[0] = new Filosofo(0);
        filosofos[1] = new Filosofo(1);
        filosofos[2] = new Filosofo(2);
        filosofos[3] = new Filosofo(3);
        filosofos[4] = new Filosofo(4);
        
        // Impressão dos talheres pertencentes a cada filosofo
        for (int i = 0; i < filosofos.length; i++) {            
            System.out.println("Talheres " + i + " - filosofo  " + i + " - talheres " + (i + 1) % 5);            
        }

        System.out.println("");    

        for (int i = 0; i < semaforos.length; i++) {
            semaforos[i] = new Semaphore(0);
        }
            
        for (int i = 0; i < filosofos.length; i++) {
            filosofos[i].start();
        }

        try {
            Thread.sleep(10000);
            System.exit(0);
        } catch (InterruptedException e) {
            Logger.getLogger(Main.class.getId()).log(Level.SEVERE, null, e);
        }
    }
}
