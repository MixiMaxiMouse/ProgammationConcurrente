package prodcons.v3;

import java.io.IOException;
import java.util.Properties;

public class TestProdCons {

	public static void main(String[] args) {

		// parsing option.xml file
		Properties properties = new Properties();
		try {
			properties.loadFromXML(TestProdCons.class.getClassLoader().getResourceAsStream("options.xml"));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("le fichier ne peut pas être chargé ...");
			return; // Stop the program if cannot load
		}

		int nProd = Integer.parseInt(properties.getProperty("nProd"));
		int nCons = Integer.parseInt(properties.getProperty("nCons"));
		int bufSz = Integer.parseInt(properties.getProperty("bufSz"));
		int prodTime = Integer.parseInt(properties.getProperty("prodTime"));
		int consTime = Integer.parseInt(properties.getProperty("consTime"));
		int minProd = Integer.parseInt(properties.getProperty("minProd"));
		int maxProd = Integer.parseInt(properties.getProperty("maxProd"));

		// create a Production Buffer of bufSize
		ProdConsBuffer buff = new ProdConsBuffer(bufSz);

		// create nProd Producers Threads
		Producer[] prods = new Producer[nProd];
		for (int i = 0; i < nProd; i++) {
			Producer p = new Producer(buff, prodTime, minProd, maxProd);
			prods[i] = p;
			p.start();
		}

		// create nCons Consumer Threads
		Consumer[] cons = new Consumer[nCons];
		for (int i = 0; i < nCons; i++) {
			Consumer c = new Consumer(buff, consTime);
			cons[i] = c;
			c.start();
		}

		// Using the interrupted flag to stop consumer threads after all the producer
		// threads finnished.
		for (Producer p : prods) {
			try {
				p.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		while (!buff.isEmpty()) {
			// Thread.yield();
		}
		
		for (Consumer c : cons) {
			c.interrupt();
		}

	}

}
