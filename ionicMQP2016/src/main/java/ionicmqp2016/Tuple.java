package ionicmqp2016;

public class Tuple {
	final String a;
	final String b;

	public Tuple (String a, String b) {
		this.a = a;
		this.b = b;
	}

	public String _1() { return a; }
	public String _2() { return b; }
}
