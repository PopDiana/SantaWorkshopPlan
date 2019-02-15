
public class Planner {

	public static void main(String[] args) {
		
		GiftTransfer giftQueue = new GiftTransfer();
		SantaClaus Santa = new SantaClaus(giftQueue);
		Workshop workshop =  new Workshop(giftQueue);
		workshop.createFactories();
		Santa.start();

	}

}
