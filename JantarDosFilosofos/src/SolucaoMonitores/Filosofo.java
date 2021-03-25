package SolucaoMonitores;

public class Filosofo implements Runnable {
    private int id;

    // Estados de um filosofo
    final int PENSANDO = 0; 
    final int FAMINTO = 1;
    final int COMENDO = 2;

	private int tempo_para_comer;
	private Monitor monitor;
	private Thread thread;
   
	Filosofo(int id, int tempo_para_comer, Monitor monitor){
		this.id = id;
		this.tempo_para_comer = tempo_para_comer;
		this.monitor = monitor;
		thread = new Thread(this);
		thread.start();
	}
   
	@Override
	public void run() {
		int cont = 1;
		while(cont <= tempo_para_comer ){
			monitor.PickUp(id);
			come(cont);
			monitor.PutDown(id);
			++cont;
		}		
	}
   
	// Impressão do estado de um filósofo.
	void come(int cont){
		System.out.format("O filósofo" + getId() + "comeu " + cont + "vezes");

		try {
		    Thread.sleep(10);
		} catch (InterruptedException e) {
           System.out.println("ERROR>" + e.getMessage());
       }
	}
	   
	private int getId() {
		return id;
    }
	
	public static void main(String[] args) {
		int tempo_para_comer = 5;
	       	
		Monitor monitor = new Monitor();
		Filosofo [] p = new Filosofo[5];
			
		System.out.println("Início do Jantar!\n");
			
		// Inicia os filósofos.
		for(int i = 0; i < 5; i++)
			p[i] = new Filosofo(i, tempo_para_comer, monitor);
			
		for(int i = 0; i < 5; i++)
			try {
				p[i].thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
	        }
	       
		System.out.println("");
		System.out.println("Fim do Jantar!");		
	}
}
