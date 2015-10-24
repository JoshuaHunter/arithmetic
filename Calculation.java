public class Calculation {

	private String first;
	private String last;
	private String operand;

	public Calculation (String f, String op, String l) {
		this.first = f;
		this.last = l;
		this.operand = op;
	}

	public String getFirst() {
		return this.first;
	}

	public String getLast() {
		return this.last;
	}

	public String getOp() {
		return this.operand;
	}

}