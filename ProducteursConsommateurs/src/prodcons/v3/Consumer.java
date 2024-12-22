package prodcons.v3;

public class Consumer extends Thread {

	// infinit loop consume one msg and sleep(ConsTime)

	int consTime;
	ProdConsBuffer buff;

	public Consumer(ProdConsBuffer buff, int consTime) {
		this.consTime = consTime;
		this.buff = buff;
	}

	@SuppressWarnings("deprecation")
	public void run() {
		System.out.println("Consumer Thread " + this.getId() + " running ...");
		while (true) {
			try {

				// break the loop if interrupted
				if (Thread.currentThread().isInterrupted())
					break;

				buff.get();
				Thread.sleep(consTime);
			} catch (InterruptedException e) {
				// break the loop if interrupted
				break;
			}
		}
	}

}
