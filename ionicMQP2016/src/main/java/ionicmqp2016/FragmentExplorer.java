package ionicmqp2016;

import java.util.Iterator;
import java.io.*;

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

	// Row code example to be compiled here...
		public static void processResultsTuple (Iterator<Tuple> it) {

			int trialNum = 0;
			while (it.hasNext()) {
				trialNum++;
				Tuple fragment = it.next();

				System.out.println ("Fragment-" + trialNum + ":\n" + fragment._1() + "::" + fragment._2());
			}
		}


	// Row code example to be compiled here...
		public static void processResults2 (Iterator<String> it, File output) {

			int trialNum = 0;
			while (it.hasNext()) {
				trialNum++;
				String fragment = it.next();

				System.out.println ("Fragment-" + trialNum + ":\n" + fragment);
			}
		}


}
