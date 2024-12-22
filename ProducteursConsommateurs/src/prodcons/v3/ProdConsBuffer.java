package prodcons.v3;

import java.util.concurrent.Semaphore;

public class ProdConsBuffer implements IProdConsBuffer {

	// AVANCEMENT 
	// semaphores mis mais pas changé le main, faut enlever les conneries de tableau et terminaison, et ca bug pour le moment :(
	Semaphore nfull;
	Semaphore nempty;
	Semaphore mutex;
	int tot;
	int in;
	int out;
	int taille;
	Message[] buffer;

	public ProdConsBuffer(int taille) {
		this.taille = taille;
		nfull = new Semaphore(0);
		nempty = new Semaphore(taille);
		mutex = new Semaphore(1);
		tot = 0;
		in = 0;
		out = 0;
		buffer = new Message[taille];
	}

	/**
	 * Put the message m in the buffer
	 **/
	@SuppressWarnings("deprecation")
	@Override
	public synchronized void put(Message m) throws InterruptedException {
		nempty.acquire(); // takes an empty space
		System.out.println(" \033[32m" + Thread.currentThread().getId() + " in the put\033[37m");
		
		// Section critique
		mutex.acquire();

		buffer[in] = m;
		in = (in + 1) % taille;
		nfull.release();
		tot++;
		// printing for debug
		System.out.println("\033[32mProduced: " + m.getMessage() + "\033[37m");
		this.prettyprint();

		mutex.release();

	}

	/**
	 * Retrieve a message from the buffer, following a FIFO order (if M1 was put
	 * before M2, M1 is retrieved before M2)
	 **/
	@Override
	public synchronized Message get() throws InterruptedException {
		nfull.acquire(); // takes a full places

		// section critique
		mutex.acquire();

		try {
			Message msg = buffer[out];
			buffer[out] = null;
			out = (out + 1) % taille;
			nempty.release();
			// printing for debug
			System.out.println("Consumed: " + msg.getMessage());
			this.prettyprint();

			return msg;

		} finally {
			mutex.release();
		}

	}

	/**
	 * Returns the number of messages currently available in the buffer
	 **/
	@Override
	public synchronized int nmsg() {
		return nfull.availablePermits();
	}

	/**
	 * Returns the total number of messages that have been put in the buffer since
	 * its creation
	 **/
	@Override
	public synchronized int totmsg() {
		return tot;
	}

	public void prettyprint() {
		System.out.println("Buffer state:");
		for (int i = 0; i < taille; i++) {
			if (buffer[i] != null) {
				System.out.println("  "+i + ") " + buffer[i].getMessage());
			} else {
				System.out.println("  "+i + ") Empty");
			}
		}
	}

	@Override
	public boolean isEmpty() {
		return nfull.availablePermits()==0;
	}

}
