
public class SantaClaus extends Thread {
	
	
	private GiftTransfer giftQueue;
	
	public SantaClaus(GiftTransfer giftQueue) {
		this.giftQueue = giftQueue;
		
	}
	
	public void run() {
		
		while(true) {
			int gift = giftQueue.receiveGift();
			System.out.println("Santa put gift " + gift + " in his sack");
		}
	}

}
