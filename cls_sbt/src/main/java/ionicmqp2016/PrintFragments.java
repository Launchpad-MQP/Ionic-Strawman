package ionicmqp2016;

import java.util.Iterator;
public class PrintFragments {
	public static void processResults (Iterator<String> it) {
		for (int i=0; it.hasNext(); i++) {
			String fragment = it.next();
			System.out.println("Fragment ("+i+"): \n"+fragment);
		}
	}
}
