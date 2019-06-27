import java.util.Random;

public class Elf extends Thread {

	private int number;
	private int X;
	private int Y;
	private int gift = 0;
	private ToyFactory factory;

	public Elf(int number, int X, int Y, ToyFactory factory) {

		this.number = number;
		this.X = X;
		this.Y = Y;
		this.factory = factory;
	}

	public void run() {

		while(true) {

			gift = gift + number;

			// Moving the elf in the factory
			factory.moveElf(this);


			// The elf sleeps 30 milliseconds after creating a gift
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}


			// Try retiring an elf from the factory
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
