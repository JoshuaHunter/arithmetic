import java.util.Scanner;
import java.util.regex.*;


public class Main {

    public static void main(String[] args) {
    	Scanner input = new Scanner(System.in);
		String sanitised = "";
		String line;
		Calculation c;
		while(input.hasNextLine()) {
			line = input.nextLine();
			if (line != null) {
				sanitised = sanitise(line);
			}
			if (sanitised == "Syntax error") {
				System.out.println(line + "\n# Syntax error");
			} else if (sanitised == "") {
				continue;
			} else {
				System.out.println(sanitised);
			}
			try {
				c = makeCalc(sanitised);
				System.out.println("# " + doCalc(c) + "\n");
			} catch (NullPointerException e) {
			}
		} 
		input.close();
	}

	public static String sanitise(String s) {
		String pattern = "(-?\\d+) ([\\+\\-h\\*\\/\\<\\>\\=]|gcd) (-?\\d+)";
		Pattern p;
		Matcher m;
		
		try {
			if (s.charAt(0) == '#') { // If it is a comment
				return "";
			}
		} catch (IndexOutOfBoundsException e) {
			return "";
		}

		p = Pattern.compile(pattern);
		m = p.matcher(s);
		if (m.matches()) {
			return s;
		}
		return "Syntax error";
	}

	public static Calculation makeCalc(String s) {
		Calculation c;
		int i, j=0;
		String first = null, operand = null, last = null;
		int psi = 0;
		for (i=0; i<s.length(); i++) {
			if (s.charAt(i) == ' ') {
				if (j == 0) {
					first = s.substring(psi, i);
					psi = i+1;
					j++;
				} else if (j == 1) {
					operand = s.substring(psi, i);
					psi = i+1;
				}
			}
		}
		last = s.substring(psi, s.length());
		return c = new Calculation(first, operand, last);
	}

	public static String doCalc(Calculation c) {
		char function = c.getOp().charAt(0);
		BigInteger first = new BigInteger(c.getFirst());
		BigInteger last = new BigInteger(c.getLast());
		String result = null;
		switch (function) {
			case '+':
				first = first.plus(last);
				result = first.toString();
				break;
			case '-':
				first = first.minus(last);
				result = first.toString();
				break;
			case '*':
				result = AwesomeBigInteger.multiply(first, last).toString();
				break;
			case 'h':
				result = first.halve().toString();
				break;
			case '/':
				try {
					BigInteger[] result_array =
						AwesomeBigInteger.divide(first, last);
					result = result_array[0].toString() + " " +
						result_array[1].toString();
				} catch (IllegalArgumentException e) {
					result = e.getMessage();
				}
				break;
			case '<':
				result = (first.lessThan(last) ? "true" : "false");
				break;
			case '>':
				result = (first.greaterThan(last) ? "true" : "false");
				break;
			case '=':
				result = (first.equalsTo(last) ? "true" : "false");
				break;
			case 'g':
				result = AwesomeBigInteger.gcd(first, last).toString();
				break;
		}
		return result;
	}

}