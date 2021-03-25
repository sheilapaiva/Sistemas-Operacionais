package SolucaoSemaforos;

public class Filosofo extends Thread {
    int id;
    
    // estados de um filosofo
    final int PENSANDO = 0; 
    final int FAMINTO = 1;
    final int COMENDO = 2;
    
    public Filosofo(int id) {
        this.id = id;
        Semaforo.estado[this.id] = 0; // O estado inicial de um filosofo eh pensando.
    }

    public void comFome() {
        Semaforo.estado[this.id] = 1;
        System.out.println("O Filósofo " + getId() + " está com fome.");
    }

    public void come() {
        Semaforo.estado[this.id] = 2;
        
        System.out.println("O Filósofo " + getId() + " está comendo.");
        
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            System.out.println("ERROR>" + e.getMessage());
        }
    }

    public void pensa() {
        Semaforo.estado[this.id] = 0;
        
        System.out.println("O Filósofo " + getId() + " está pensando.");
        
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            System.out.println("ERROR>" + e.getMessage());
        }
    }

    public void largarTalher() throws InterruptedException {
        Semaforo.mutex.acquire();
        pensa();
        
        /* Quando um filosofo largar os talheres, ele deve checar se um dos vizinhos deve ser 
        acordado. Para isso acontecer, um dos vizinhos deve estar com fome e o outro não 
        estiver comendo. */
        
        Semaforo.filosofos[vizinhoEsquerda()].tentarObterTalheres();
        Semaforo.filosofos[vizinhoDireita()].tentarObterTalheres();
        Semaforo.mutex.release();
    }

    public void pegarTalheres() throws InterruptedException {
        Semaforo.mutex.acquire();
        comFome();

        /* O filosofo tentara obter os talheres caso a condição for verdadeira, ou seja, semaforo(1) 
        isso permitira que o filosofo obtenha os talheres. */
        tentarObterTalheres();        
        Semaforo.mutex.release();

        /* Caso a condição não seja verdadeira, o filosofo vai ficar bloqueado no seu respectivo índice
        no semáforo até chegar novamente sua vez e tentar obeter os talheres. */
        Semaforo.semaforos[this.id].acquire();
    }

    public void tentarObterTalheres() {
        /* Caso o filosofo esteja com fome e nenhum de seus vizinhos estiverem comendo, então será
        possível que o filosofo coma. */
        if (Semaforo.estado[this.id] == 1 && Semaforo.estado[vizinhoEsquerda()] != 2 && Semaforo.estado[vizinhoDireita()] != 2) {
            come();
            Semaforo.semaforos[this.id].release();
        } else {
            System.out.println(getId() + " não conseguiu comer!");
        }

    }

    public int vizinhoDireita() {
        return (this.id + 1) % 5;
    }

    public int vizinhoEsquerda() {
        if (this.id == 0) {            
            return 4;
        } else {
            return (this.id - 1) % 5;
        }
    }

    @Override
    public void run() {
        try {
            pensa();
            System.out.println("");
            do {
                pegarTalheres();
                Thread.sleep(1000L);
                largarTalher();
            } while (true);
        } catch (InterruptedException e) {
            System.out.println("ERROR>" + e.getMessage());
            return;
        }
    }

}
