package SolucaoMonitores;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class Monitor {
	static int[] estado = new int[5];

	final Lock lock;
	final Condition [] cond;	

	Monitor(){
		lock = new ReentrantLock();
		cond = new Condition[5];
		
		// Definição do estado inicial de cada filosofo
		for (int i = 0; i < estado.length; i++) {
            estado[i] = 0;
        }
	}

	public void PickUp(int i){
		lock.lock();
		try {
			estado[i] = 1;
			
			if ((estado[(i-1+5) % 5] != 2 ) && (estado[(i+1) % 5] != 2) ) {
				estado[i] = 2;
			} 
			else {	// Se pelo menos um dos vizinhos estiver comendo, então o filósofo deve esperar.
				try {
					cond[i].await();

					// Após esperar, ele deve comer.
					estado[i] = 2;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			}
		}
		finally{
			lock.unlock();
		}
	}

	public void PutDown(int i){
		lock.lock();
		try {
			estado[i] = 0;

			// Avisa ao filósofo esquerdo se pode comer 
			int left = (i - 1 + 5) % 5;
			int left2 = (i - 2 + 5) % 5;
			if((estado[left] == 1) && (estado[left2] != 2)){
				cond[left].signal();
			}

			// Avisa ao filósofo direito se pode comer 
			if((estado[(i+1) % 5] == 1) && (estado[(i+2) %5 ] != 2) ){
				cond[(i+1) % 5].signal();
			}
		}
		finally {
			lock.unlock();
		}
	}

}
