public class Planner {

	public static void main(String[] args) {

		// Creating the gift transfer queue
		GiftTransfer giftQueue = new GiftTransfer();

		// Creating Santa (with help from God)
		SantaClaus Santa = new SantaClaus(giftQueue);

		// Creating Santa's workshop
		Workshop workshop =  new Workshop(giftQueue);

		// Starting factory creation
		workshop.createFactories();

		// Santa starts receiving gifts
		Santa.start();

	}

}
