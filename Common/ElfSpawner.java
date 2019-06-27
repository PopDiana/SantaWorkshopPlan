import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class ElfSpawner extends Thread{

	private ToyFactory factory;

	public ElfSpawner(ToyFactory factory) {
		this.factory = factory;
	}

	public void run() {

		while(true) {

			Random rand = new Random();
			long milis = rand.nextInt(1000) + 500;


			// Sleeping a random time between 500 and 1000 milliseconds
			try {
				Thread.sleep(milis);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// Spawning an elf
			spawnAnElf();
		}
	}

	private void spawnAnElf() {

		Random rand = new Random();


		// Getting the factory lock
		ReentrantLock factoryLock = factory.getFactoryLock();

		// Elves can't move while adding a new elf in the factory
		factoryLock.lock();

		// Getting the factory matrix size
		int factorySize = factory.getN();


		if(factory.nrExistingElves() != factorySize / 2) {

			// Randomizing a position for the elf
			int X = rand.nextInt(factorySize) + 0;
			int Y = rand.nextInt(factorySize) + 0;


			// Getting the elves counter lock
			ReentrantLock elvesCounterLock = Workshop.getElvesCounterLock();

			// No other thread can access the number of elves in the factory
			// since it's being modified now
			elvesCounterLock.lock();

			// Creating a new elf
			Elf elf = new Elf(Workshop.nrTotalElves, X, Y, factory);

			// Try inserting the elf in the factory
			if(factory.addElf(elf)) {

				Workshop.nrTotalElves++;
				System.out.println("Elf " + elf.getNumber() +
						" was created in factory " + factory.getNumber());
			}

			// Unlock the elves counter
			elvesCounterLock.unlock();

		}
		// Unlock the factory
		factoryLock.unlock();

	}


}
