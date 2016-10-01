package ionicmqp2016;

import java.util.Iterator;
public class FragmentExplorer {

	

	// Row code example to be compiled here...
	public static void processResults (Iterator<String> it) {

		int trialNum = 0;
		while (it.hasNext()) {
			trialNum++;
			String fragment = it.next();

			System.out.println ("Fragment-" + trialNum + ":\n" + fragment);
		}
	}


}
