import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class ElfSpawner extends Thread{
	
	private ToyFactory factory;
	private CyclicBarrier elfBarrier;
	
	
	public ElfSpawner(ToyFactory factory) {
		this.factory = factory;
		elfBarrier = new CyclicBarrier(factory.getN());
	}
	
	
	
	public void run() {
		
		while(true) {
			Random rand = new Random();
			long milis = rand.nextInt(1000) + 500;
			try {
				Thread.sleep(milis);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			spawnAnElf();
		}
	}
	
	private void spawnAnElf() {
		
		Random rand = new Random();
		
		ReentrantLock factoryLock = factory.getFactoryLock();
		
		factoryLock.lock();
		
		int factorySize = factory.getN();
		
		if(factory.nrExistingElves() != factorySize) {
			
			// Modify factory to contain maximum N elves 
			// or eliminate elf from elfList while it's sleeping
			// and reinsert it after the barrier is passed
			
			int X = rand.nextInt(factorySize) + 0;
			int Y = rand.nextInt(factorySize) + 0;
			
			ReentrantLock elvesCounterLock = Workshop.getElvesCounterLock();
			elvesCounterLock.lock();
			
			Elf elf = new Elf(Workshop.nrTotalElves, X, Y, factory, elfBarrier);
			
			if(factory.addElf(elf)) {
				
				Workshop.nrTotalElves++;
				System.out.println("Elf " + elf.getNumber() + 
						" was created in factory " + factory.getNumber());
			}
			
			elvesCounterLock.unlock();				
			
		}
		
		factoryLock.unlock();
		
	}
	

}
