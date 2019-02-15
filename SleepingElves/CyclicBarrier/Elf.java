import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Elf extends Thread {

	private int number;
	private int X;
	private int Y;
	private int gift = 0;
	private ToyFactory factory;
	private CyclicBarrier elfBarrier;
	
	public Elf(int number, int X, int Y, ToyFactory factory, CyclicBarrier elfBarrier) {
		
		this.number = number;
		this.X = X;
		this.Y = Y;
		this.factory = factory;
		this.elfBarrier = elfBarrier;
	}
	
	
	public void run() {
		
		while(true) {
			
			if(Math.abs(X - Y) <= 1) {
				
				System.out.println("Elf " + number + 
						" is sleeping on position (" + X + "," + Y + ")" );
				try {
						
					elfBarrier.await();
						
				} catch (InterruptedException | BrokenBarrierException e) {
					
					e.printStackTrace();
				}
			}
			
			gift = gift + number;

			factory.moveElf(this);
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(Workshop.elfRetireSemaphore.tryAcquire()) {
				factory.retireElf(this);
				break;
			}
		}
	}
	
	
	public void changePosition(int newX, int newY) {
		
		X = newX;
		Y = newY;
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
