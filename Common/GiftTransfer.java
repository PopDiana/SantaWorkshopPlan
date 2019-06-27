public class GiftTransfer {

	private volatile int head = 0;
	private volatile int tail = 0;
	private int[] gifts = new int[10];

	public synchronized int receiveGift() {

		int gift = 0;

		// Waiting until the buffer is no longer empty
		while(tail == head) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// Getting the gift from the buffer
		gift = gifts[head % gifts.length];
		head++;

		// Notify that the buffer is not full
		notifyAll();

		return gift;
	}

	public synchronized void giveGift(int gift) {

			// Waiting until the buffer is no longer full
			while(tail - head == gifts.length) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			// Adding the gift in the buffer
			gifts[tail % gifts.length] = gift;
			tail++;

			// Notify that the buffer is not empty
			notifyAll();

	}
}
