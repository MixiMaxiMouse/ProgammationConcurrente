package prodcons.v5;

public class ProdConsBuffer implements IProdConsBuffer {

	/*
	 * Tableau de Garde Action :
	 * 
	 * |methode | garde | post action |
	 * 
	 * -------------------------------------- |
	 * 
	 * | put() | nempty > 0| nempty--; ...|
	 * 
	 * | get(k) | nfull >= k | nfull-k; ...|
	 * 
	 */

	int nfull;
	int nempty;
	int tot;
	int in;
	int out;
	int taille;
	Message[] buffer;

	public ProdConsBuffer(int taille) {
		this.taille = taille;
		nfull = 0;
		nempty = taille;
		tot = 0;
		in = 0;
		out = 0;
		buffer = new Message[taille];
	}

	/**
	 * Put the message m in the buffer
	 **/
	@Override
	public synchronized void put(Message m) throws InterruptedException {
		System.out.println(" \033[32m" + Thread.currentThread().getId() + " in the put\033[37m");
		
		// garde
		while (nempty <= 0) {
			System.out.println(" \033[32m \t" + Thread.currentThread().getId() + " is trying to put ...\033[37m");
			wait();
		}

		// post-action
		buffer[in] = m;
		in = (in + 1) % taille;
		nfull++;
		nempty--;
		tot++;

		// printing for debug
		System.out.println("\033[32mProduced: " + m.getMessage() + "\033[37m");
		this.prettyprint();

		notifyAll();
	}

	/**
	 * Retrieve a messages from the buffer, following a FIFO order (if M1 was put
	 * before M2, M1 is retrieved before M2)
	 **/
	@Override
	public Message get() throws InterruptedException {
		return get(1)[0];
	}

	/**
	 * Retrieve k messages from the buffer, following a FIFO order (if M1 was put
	 * before M2, M1 is retrieved before M2)
	 **/
	@Override
	public synchronized Message[] get(int k) throws InterruptedException {
		System.out.println(" \033[94m" + Thread.currentThread().getId() + " in the get\033[37m");
		// garde
		while (nfull < k){
			System.out.println(" \033[94m \t" + Thread.currentThread().getId() + " is trying to get ...\033[37m");
			wait();
		}

		// post-action
		Message[] msgs = new Message[k];
		for (int i = 0; i < k; i++) {
			Message msg = buffer[out];
			msgs[i] = msg;
			buffer[out] = null; // clears out the messages gotten
			System.out.println("Consumed: " + msg.getMessage());
			out = (out + 1) % taille;
		}
		nfull -= k;
		nempty += k;

		// printing for debug
		this.prettyprint();

		notifyAll();
		return msgs;
	}

	/**
	 * Returns the number of messages currently available in the buffer
	 **/
	@Override
	public synchronized int nmsg() {
		return nfull;
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
				System.out.println(i + ") " + buffer[i].getMessage());
			} else {
				System.out.println(i + ") Empty");
			}
		}
	}
}
