import java.util.Random;
import java.util.concurrent.Semaphore;


public class Elf extends Thread {

	private int number;
	private int X;
	private int Y;
	private int gift = 0;
	private ToyFactory factory;
	private static int counter = 0;
	private static Semaphore counterSemaphore = new Semaphore(1);
	
	
	
	public Elf(int number, int X, int Y, ToyFactory factory) {
		
		this.number = number;
		this.X = X;
		this.Y = Y;
		this.factory = factory;
		
	}
	
	
	public void run() {
		
		while(true) {
			
			if(Math.abs(X - Y) <= 1) {
				System.out.println("Elf " + number + 
						" is sleeping on position (" + X + "," + Y + ")" );
				
				
				
				if(counterSemaphore.tryAcquire()) {
					
					counter++;
					counterSemaphore.release();
					
				}else {
					
					continue;
				}
				
				while(counter < factory.getN()) {
					// Wait for other elves
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
									
			}
			
			gift = gift + number;
			
			factory.moveElf(this);
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			/*if(Workshop.elfRetireSemaphore.tryAcquire()) {
				factory.retireElf(this);
				break;
			}*/
		}
	}
	
	
	public void changePosition(int X, int Y) {
		
		this.X = X;
		this.Y = Y;
	}
	
	public int getNumber() {
		return number;
	}
	
	public int getX() {
		return X;
	}
	
	public int getY() {
		return Y;
	}
	
	public int getGift() {
		return gift;
	}

	public void stopWork() {
		
		Random rand = new Random();
		long milis = rand.nextInt(50) + 10;
		try {
			Thread.sleep(milis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	public void reportPosition() {
		
		System.out.println("Elf " + number + 
				" is at (" + X + "," + 
				Y + ") in factory " + factory.getNumber() );		
	}
	
	
}
