import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class ToyFactory extends Thread{

	private int number;
	private int N;
	private boolean factory[][];
	private ArrayList<Elf> elves = new ArrayList<Elf>();
	private ArrayList<Integer> gifts = new ArrayList<Integer>();
	private ReentrantLock factoryLock = new ReentrantLock();
	private ReentrantLock elvesListLock = new ReentrantLock();
	private Semaphore reindeerSemaphore = new Semaphore(10);
	private ReentrantLock giftsLock = new ReentrantLock();

	public ReentrantLock getFactoryLock() {
		return factoryLock;
	}

	public ToyFactory(int N, int number) {

		this.factory = new boolean[N][N];
		this.number = number;
		this.N = N;
	}

	public int nrExistingElves() {
		return elves.size();
	}

	public int getN() {
		return N;
	}

	public int getNumber() {
		return number;
	}

	public void run() {

		while(true) {

			try {
				// Try asking existing elves for their position
				// (the factory is also a working thread)
				askElvesForPosition();

				// Sleeping for 3000 milliseconds
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	public void moveElf(Elf elf) {

		// Getting elf info
		int X = elf.getX();
		int Y = elf.getY();
		int gift = elf.getGift();
		int elfNumber = elf.getNumber();


		try {

			// Only an elf can move at a time in the factory
			factoryLock.lock();


			if(canMoveRight(X, Y)) {

				// Change position in matrix
				factory[X][Y] = false;
				factory[X][Y + 1] = true;

				createGift(gift, elfNumber);

				// Change elf's current position
				elf.changePosition(X, Y + 1);

				// Ask all elves for position
				askElvesForPosition();

			}else if(canMoveUp(X, Y)) {

				// Change position in matrix
				factory[X][Y] = false;
				factory[X - 1][Y] = true;

				createGift(gift, elfNumber);

				// Change elf's current position
				elf.changePosition(X - 1, Y);

				// Ask all elves for position
				askElvesForPosition();

			}else if(canMoveDown(X, Y)) {

				// Change position in matrix
				factory[X][Y] = false;
				factory[X + 1][Y] = true;

				createGift(gift, elfNumber);

				// Change elf's current position
				elf.changePosition(X + 1, Y);

				// Ask all elves for position
				askElvesForPosition();

			}else if(canMoveLeft(X, Y)) {

				// Change position in matrix
				factory[X][Y] = false;
				factory[X][Y - 1] = true;

				createGift(gift, elfNumber);

				// Change elf's current position
				elf.changePosition(X, Y - 1);

				// Ask all elves for position
				askElvesForPosition();

			}else {

				// Elf can't move (its's surrounded), it stops working
				elf.stopWork();
			}

		} finally {

			factoryLock.unlock();

		}

	}

	private boolean canMoveLeft(int X, int Y) {

		if(Y - 1 > 0) {
			if(!factory[X][Y - 1]) {
				return true;
			}
		}

		return false;
	}

	private boolean canMoveDown(int X, int Y) {

		if(X + 1 < N) {
			if(!factory[X + 1][Y]) {
				return true;
			}
		}
		return false;
	}

	private boolean canMoveUp(int X, int Y) {

		if(X - 1 > 0) {
			if(!factory[X - 1][Y]) {
				return true;
			}
		}
		return false;
	}

	private boolean canMoveRight(int X, int Y) {

		if(Y + 1 < N) {
			if(!factory[X][Y + 1]) {
				return true;
			}
		}
		return false;
	}

	public boolean addElf(Elf elf) {

		// Other threads can't access the elves' list while an elf is
		// being added to it
		// Reporting can't be done while an elf is spawned
		elvesListLock.lock();

		int X = elf.getX();
		int Y = elf.getY();



		if(factory[X][Y]) {

			elvesListLock.unlock();
			return false;

		}else {

			factory[X][Y] = true;

			elves.add(elf);

			elf.start();

			elf.reportPosition();

			elvesListLock.unlock();

			return true;
		}


	}

	private void askElvesForPosition() {

		try {

			// No new elves can be added while asking the current ones
			// Elves can't move while reporting their position
			// Reindeers can't get gifts while factory is asking elves for position
			factoryLock.lock();
			elvesListLock.lock();
			giftsLock.lock();


			for(Elf elf : elves) {
				elf.reportPosition();
			}

		} finally {

			elvesListLock.unlock();
			factoryLock.unlock();
			giftsLock.unlock();

		}

	}

	public int getGift() {

		int gift = 0;

		try {

			// Acquire a reindeer permit
			try {
				reindeerSemaphore.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// Two reindeer can't read the gifts list at the same time
			giftsLock.lock();

			try {
				gift = gifts.get(gifts.size() - 1);
				gifts.remove(gifts.size() - 1);
			}catch(Exception exception) {
				// The gifts list is empty
				gift = 0;
			}

		} finally {

			giftsLock.unlock();
			reindeerSemaphore.release();

		}

		return gift;
	}


	private void createGift(int gift, int elfNumber) {

		try {
			// Reindeers can't get gifts while an elf creates a gift
			giftsLock.lock();
			gifts.add(gift);
			System.out.println("Elf " + elfNumber + " created gift " + gift);

		} finally {

			giftsLock.unlock();

		}


	}

	public void retireElf(Elf elf) {

		try {

			// Modifying the elves' list and factory matrix
			elvesListLock.lock();
			factoryLock.lock();

			elves.remove(elf);

			int X = elf.getX();
			int Y = elf.getY();

			factory[X][Y] = false;

			System.out.println("Elf " + elf.getNumber() +
					" retired from factory " + number);


		}finally {

			elvesListLock.unlock();
			factoryLock.unlock();

		}
	}
}



