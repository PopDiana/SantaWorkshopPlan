public class ElfRetirement extends Thread {
	
	
	
	public void run() {
		
		while(true) {
			Workshop.elfRetireSemaphore.release();
		
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	

}
