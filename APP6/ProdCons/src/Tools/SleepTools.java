package Tools;

/**
 * @author Departement GEGI Sherbrooke
 * @version 1.1
 */
public class SleepTools
{
	/**
	 * Static call to the nap method with default nap time for NAP_TIME secondes
	 * 
	 */
	public static void nap() {
		nap(NAP_TIME);
	}

	/**
	 * Make the thread sleep for NAP_TIME * Math.random(). 
	 * 
	 * @param duration This parameter is unused. 
	 */
	public static void nap(int duration) {
        	int sleeptime = (int) (duration * Math.random() );
        	try { Thread.sleep(sleeptime*1000); }
        	catch (InterruptedException e) {}
	}

	/**
	 * NAP_TIME is initialized at 5 second
	 */
	private static final int NAP_TIME = 5;
}
