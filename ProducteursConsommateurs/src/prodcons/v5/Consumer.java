package prodcons.v5;

import java.util.concurrent.ThreadLocalRandom;

public class Consumer extends Thread {

	// infinit loop consume one msg and sleep(ConsTime)

	int consTime;
	int maxGet;
	ProdConsBuffer buff;

	// maxGet is the maximum number a consumer thread can get at a time. number of a
	// get is decided randomly between 0 and maxGet included
	public Consumer(ProdConsBuffer buff, int consTime, int maxGet) {
		this.consTime = consTime;
		this.buff = buff;
		this.maxGet = maxGet;
	}

	@SuppressWarnings("deprecation")
	public void run() {
		System.out.println("Consumer Thread " + this.getId() + " running ...");
		while (true) {
			try {

				// break the loop if interrupted
				if (Thread.currentThread().isInterrupted()) {
					System.out.println("Consumer Thread " + this.getId() +" has been stopped.");
					break;
				}
				
				int k = ThreadLocalRandom.current().nextInt(0, this.maxGet + 1);
				buff.get(k);
				Thread.sleep(consTime);
			} catch (InterruptedException e) {
				System.out.println("Consumer Thread " + this.getId() +" has been stopped");
				break;
			}
		}
	}

}
