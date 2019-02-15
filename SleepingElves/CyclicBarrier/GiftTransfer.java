
public class GiftTransfer {

	private volatile int head = 0;
	private volatile int tail = 0;
	private int[] gifts = new int[10];
	
	public synchronized int receiveGift() {
		
		int gift = 0;
		
		while(tail == head) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
			
		gift = gifts[head % gifts.length];
		head++;
			
		notifyAll();
		
		return gift;
	}
	

	public synchronized void giveGift(int gift) {
				
			while(tail - head == gifts.length) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			gifts[tail % gifts.length] = gift;
			tail++;
					
			notifyAll();
						
	} 
}
