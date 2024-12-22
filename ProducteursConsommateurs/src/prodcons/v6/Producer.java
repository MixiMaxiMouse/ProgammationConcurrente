package prodcons.v6;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class Producer extends Thread {

	// Produce 1 msg every prodTime seconds between minProd and maxProd times
	// (randomly)

	int prodTime;
	int minProd;
	int maxProd;
	int maxPut;
	ProdConsBuffer buff;
	Semaphore mutex;
	Semaphore rdv;
	int n_messages;
	int n_tot;

	public Producer(ProdConsBuffer buff, int prodTime, int minProd, int maxProd, int maxPut) {
		this.buff = buff;
		this.prodTime = prodTime;
		this.minProd = minProd;
		this.maxProd = maxProd;
		this.maxPut = maxPut;
		rdv = new Semaphore(0);
		mutex = new Semaphore(1);
	}

	@SuppressWarnings("deprecation")
	public void come() {
		try {
			mutex.acquire();
			n_messages--;
			if (n_messages + 1 == 0) { // we wait for nconsumers plus the one producer
				rdv.release(n_tot);
				System.out.println("\033[33m" + Thread.currentThread().getId() + " rdv released \033[37m");
			}
			mutex.release();
			rdv.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("deprecation")
	public void run() {
		int n = ThreadLocalRandom.current().nextInt(minProd, maxProd + 1);
		for (int i = 0; i < n; i++) {
			String s = "Message from Producer " + this.getId(); // adding ID for debug
			try {
				n_messages = ThreadLocalRandom.current().nextInt(1, maxPut + 1);
				n_tot = n_messages + 1;
				buff.put(new Message(s, this), n_messages);
				Thread.sleep(prodTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
