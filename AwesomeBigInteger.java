import java.util.ArrayList;

public class AwesomeBigInteger {

	public static BigInteger gcd(BigInteger a, BigInteger b) {
		a = a.magnitude();
		b = b.magnitude();
		while(true) {
			if(a.greaterThan(b)) {
				a = divide(a, b)[1];
				if(a.equalsTo(BigInteger.ZERO)) {
					return b;
				}
			} else {
				b = divide(b, a)[1];
				if(b.equalsTo(BigInteger.ZERO)) {
					return a;
				}
			}
		}
	}

	public static BigInteger multiply(BigInteger a, BigInteger b) {
		int i;
		BigInteger zero = new BigInteger(0);
		BigInteger result = new BigInteger(0);
		BigInteger half_a = a.plus(result);
		BigInteger double_b = b.plus(result);
		BigInteger prev_half = a.plus(a);
		while (!half_a.equalsTo(BigInteger.ZERO)) {
			prev_half = half_a.plus(zero);
			half_a = half_a.halve();

			if (!half_a.plus(half_a).equalsTo(prev_half)) {
				result = result.plus(double_b);
			}
			double_b = double_b.plus(double_b);
		}
		result.isNegative = a.isNegative ^ b.isNegative;
		return result;
	}

	public static BigInteger[] divide(BigInteger a, BigInteger b) {
		BigInteger[] result = new BigInteger[2];
		result[0] = BigInteger.ZERO;
		result[1] = BigInteger.ZERO;


		boolean result_negitive = a.isNegative ^ b.isNegative;
		a.isNegative = b.isNegative = false;

		if (a.equalsTo(result[0])) { // if a == 0 return 0
			return result;
		} else if (b.equalsTo(result[0])) { // if b == 0 throw exception
			throw new IllegalArgumentException ("Cannot divide by zero");
		} else if (a.equalsTo(b)) { // if a == b return 1
			result[0] = new BigInteger(1);
			result[0].isNegative = result_negitive;
			return result;
		} else if (b.greaterThan(a)) { // if a < b remainder = a, result = 0
			result[1] = result[1].plus(a);
			return result;
		}


		BigInteger right_index = BigInteger.ZERO.plus(b);

		/* base case: a >= b */
		BigInteger index = new BigInteger(1);
		while(right_index.lessThan(a)) {
			//System.out.println("While B: " + right_index + "\tINDEX: " + index);
			right_index = right_index.plus(right_index);
			index = index.plus(index);
		}

		//System.out.println("B: " + right_index + "\tINDEX: " + index);

		while(!right_index.equalsTo(b)) {
			right_index = right_index.halve();
			index = index.halve();
			BigInteger aminusb = a.minus(right_index);
			//System.out.println("A: " + a + "\tB: " + right_index + "\ta minus b: " + aminusb);
			if(aminusb.equalsTo(BigInteger.ZERO) || aminusb.greaterThan(BigInteger.ZERO)) {
				a = aminusb;
				//System.out.println("result: " + result[0] + "\tindex: " + index);
				result[0] = result[0].plus(index);
			}

			//System.out.println();
		}

		result[1] = a;

		if(a.equalsTo(b)) {
			result[1] = BigInteger.ZERO;
			result[0] = result[0].plus(BigInteger.ONE);
		}

		result[0].isNegative = result_negitive;
		return result;
	}

}
