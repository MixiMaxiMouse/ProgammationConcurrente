package prodcons.v1;

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
				buff.get();
				Thread.sleep(consTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
