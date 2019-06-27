import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class Workshop {

	public static int nrFactories;
	private ToyFactory factories[];
	private ElfSpawner spawners[];
	public static volatile int nrTotalElves = 1;
	private static ReentrantLock elvesCounterLock = new ReentrantLock();
	private Reindeer reindeers[];
	private GiftTransfer giftQueue;
	public static volatile Semaphore elfRetireSemaphore = new Semaphore(0);
	private ElfRetirement elfRetire = new ElfRetirement();


	public Workshop(GiftTransfer giftQueue) {
		this.giftQueue = giftQueue;
	}

	public static ReentrantLock getElvesCounterLock() {
		return elvesCounterLock;
	}

	public void createFactories() {

		Random rand = new Random();
		nrFactories = rand.nextInt(4) + 2;
		int nrReindeers = rand.nextInt(10) + 8;

		factories = new ToyFactory[nrFactories];
		spawners = new ElfSpawner[nrFactories];
		reindeers = new Reindeer[nrReindeers];


		System.out.println("There were created " + nrFactories + " factories");
		System.out.println("There were created " + nrReindeers + " reindeers");

		for(int i = 0; i < nrFactories ; ++i) {

			int N = rand.nextInt(500) + 100;
			factories[i] = new ToyFactory(N, i + 1);
			spawners[i] = new ElfSpawner(factories[i]);
			System.out.println("Factory " + (i + 1) + " has N = " + N);
		}

		for(int i = 0; i < nrReindeers ; ++i) {
			reindeers[i] = new Reindeer(factories, i + 1, giftQueue);
		}

		for(int i = 0; i < nrFactories ; ++i) {
			spawners[i].start();
			factories[i].start();
		}

		for(int i = 0; i < nrReindeers ; ++i) {
			reindeers[i].start();
		}

		elfRetire.start();

	}


}
