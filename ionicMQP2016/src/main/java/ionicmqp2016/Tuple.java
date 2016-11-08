package ionicmqp2016;
import java.io.File;
import java.io.PrintWriter;

public class Tuple {
	final String a;
	final String b;
	final static String BASEPATH = "../";

	public Tuple (String a, String b) {
		this.a = a;
		this.b = b;
	}

	public String toString() {
		return b + ": " + a.split("\n", 1);
	}

	//creates a file with the given contents at the given path
	public void createFile() {
		try {
			PrintWriter writer = new PrintWriter(BASEPATH + b, "UTF-8");
			writer.print(a);
			writer.close();
			System.out.println("Created file " + BASEPATH + b);
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("Couldn't write to file.");
		}
	}
}
