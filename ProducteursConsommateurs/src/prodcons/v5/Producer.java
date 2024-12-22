package prodcons.v5;

import java.util.concurrent.ThreadLocalRandom;

public class Producer extends Thread {

	// Produce 1 msg every prodTime seconds between minProd and maxProd times
	// (randomly)

	int prodTime;
	int minProd;
	int maxProd;
	ProdConsBuffer buff;

	public Producer(ProdConsBuffer buff, int prodTime, int minProd, int maxProd) {
		this.buff = buff;
		this.prodTime = prodTime;
		this.minProd = minProd;
		this.maxProd = maxProd;
	}

	@SuppressWarnings("deprecation")
	public void run() {
		int n = ThreadLocalRandom.current().nextInt(minProd, maxProd + 1);
		for (int i = 0; i < n; i++) {
			String s = "Message from Producer " + this.getId(); // adding ID for debug
			try {
				buff.put(new Message(s));
				Thread.sleep(prodTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
