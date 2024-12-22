package prodcons.v1;

import java.io.IOException;
import java.util.Properties;

public class TestProdCons {

	public static void main(String[] args) {

		// parsing option.xml file
		Properties properties = new Properties();
		try {
			properties.loadFromXML(TestProdCons.class.getClassLoader().getResourceAsStream("options.xml"));
		} catch (IOException e) {
			System.out.println("le fichier ne peut pas être chargé ...");
			e.printStackTrace();
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
		for (int i = 0; i < nProd; i++) {
			new Producer(buff, prodTime, minProd, maxProd).start();
		}

		// create nCons Consumer Threads
		for (int i = 0; i < nCons; i++) {
			new Consumer(buff, consTime).start();
		}

	}

}
