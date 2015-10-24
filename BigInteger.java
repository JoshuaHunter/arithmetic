import java.util.*;

public class BigInteger {

    private ArrayList<Integer> values;
    boolean isNegative;
    public static final int NUMBER_CHAR = 1;
    public static final int MAX = 9;//(10 ^ NUMBER_CHAR) - 1;
    public static final BigInteger ZERO = new BigInteger(0);
    public static final BigInteger ONE = new BigInteger(1);

    public BigInteger() {
        values = new ArrayList<Integer>();
    }


    public BigInteger(int i) {
        values = new ArrayList<Integer>();
        if(i < 0) {
            isNegative = true;
            values.add(-i);
        } else {
            isNegative = false;
            values.add(i);
        }
    }

    public BigInteger(String input) {
        values = new ArrayList<Integer>();

        isNegative = false;
        if(input.charAt(0) == '-') {
            isNegative = true;
            input = input.substring(1);
        }

        for(int i = input.length(); i > 0; i -= NUMBER_CHAR) {
            int to = i - NUMBER_CHAR;
            if(to < 0) {
                to = 0;
            }
            int toAdd = Integer.parseInt(input.substring(to, i));
            values.add(toAdd);
        }
        trim_leading_zeros();
    }

    public String toString() {
		if (values.isEmpty()) {
			return "0";
		}
        StringBuilder sb = new StringBuilder();
        if(isNegative) {
            sb.append('-');
        }
        for(int i = values.size() - 1; i >= 0; i--) {
            sb.append(values.get(i));
        }
        return sb.toString();
    }

    public void trim_leading_zeros() {
		int i;
		for (i=values.size()-1; i>0; i--) {
			if (values.get(i) == 0) {
				values.remove(i);
			} else {
				return;
			}
		}
	}

	/*
	 *
	 * Make halve() return a copy * * * * * * * * ** * * * * ** * * * * * * **
	 *
	 */
	public BigInteger halve() {
		int i;
		int curr;
		boolean isOdd = false;
		BigInteger result = new BigInteger();
		result.values = new ArrayList<Integer>(values.size());
		for(int val : values) {
			result.values.add(val);
		}
		for (i=values.size()-1; i>=0; i--) {
			curr = values.get(i);
			result.values.set(i, curr/2 + (isOdd ? 5 : 0));
			isOdd = curr % 2 == 1;
		}
		result.trim_leading_zeros();
        result.isNegative = isNegative;
		return result;
	}

	private BigInteger signless_plus(BigInteger other) {
		ArrayList<Integer> smallest, largest;
		BigInteger result = new BigInteger();
		int smallest_curr_value;
        int increment = 0;
        int max;

        if (this.signlessGreaterThan(other)) {
			largest = this.values;
			smallest = other.values;
		} else {
			smallest = this.values;
			largest = other.values;
		}

		max = largest.size();

        for(int i = 0; i < max; i++) {
			try {
				smallest_curr_value = smallest.get(i);
			} catch (IndexOutOfBoundsException e) {
				smallest_curr_value = 0;
			}

            int newValue = largest.get(i) + smallest_curr_value + increment;
            if(newValue > MAX) {
                newValue -= MAX+1;
                increment = 1;
            } else {
				increment = 0;
			}
            result.values.add(newValue);
        }
        if (increment == 1) {
			result.values.add(1);
		}
        return result;
	}

	private boolean isZero() {
        if(values.size() == 0) {
            return true;
        }
		return (values.size() == 1 && values.get(0) == 0);
	}

    public BigInteger plus(BigInteger other) {
		ArrayList<Integer> smallest, largest;
		BigInteger result;
		int smallest_curr_value;
        int increment = 0;
        int max;

        if (isNegative && other.isNegative) {
			result = this.signless_plus(other);
			result.isNegative = true;
			return result;
		}
		if (isNegative && !other.isNegative) {
			return other.signless_minus(this);
		}
		if (!isNegative && other.isNegative) {
			return this.signless_minus(other);
		}
		return signless_plus(other);
    }

    private BigInteger signless_minus(BigInteger other) {
    //    System.out.println(this + "\t-\t" + other);
		BigInteger result = new BigInteger();
		ArrayList<Integer> smallest, largest;
        int decrement = 0;
        int max;
        if (this.signlessGreaterThan(other)) {
        //    System.out.println("GT");
			largest = this.values;
			smallest = other.values;
		} else {
			smallest = this.values;
			largest = other.values;
			result.isNegative = true;
		}

		max = largest.size();

        for (int i = 0; i < max; i++) {
			int smallest_curr_value;
			try {
				smallest_curr_value = smallest.get(i);
			} catch (IndexOutOfBoundsException e) {
				smallest_curr_value = 0;
			}
            int newValue = largest.get(i) - smallest_curr_value - decrement;
            if (newValue < 0) {;
                newValue = MAX + 1 + newValue;
                decrement = 1;
            } else {
                decrement = 0;
            }
            result.values.add(newValue);
        }
        result.trim_leading_zeros();
		return result;
	}

    public BigInteger minus(BigInteger other) {
		BigInteger result;
		if (this.equalsTo(other)) {
			return new BigInteger();
		}

		if (isNegative && other.isNegative) {
			return other.signless_minus(this);
		}
		if (isNegative && !other.isNegative) {
			result = this.signless_plus(other);
			result.isNegative = true;
			return result;
		}
		if (!isNegative && other.isNegative) {
			return this.signless_plus(other);
		}
		return this.signless_minus(other);
    }

    public BigInteger magnitude() {
		BigInteger result = new BigInteger();
		result.values = new ArrayList<Integer>(values.size());
		for(int val : values) {
			result.values.add(val);
		}
		return result;
	}

    public boolean equalsTo(BigInteger other) {
		int i;
		if (isZero() && other.isZero()) {
			return true;
		}
		if (isNegative != other.isNegative) {
			return false;
		}
		if (values.size() != other.values.size()) {
			return false;
		}
		for (i=0; i<values.size(); i++) {
			if (values.get(i) != other.values.get(i)) {
				return false;
			}
		}
		return true;
	}

    public boolean greaterThan(BigInteger other) {
        boolean result;
        int i;
        if (this.equalsTo(other)) {
			return false;
		}
		if (isNegative && !other.isNegative) {
			return false;
		}
		if (!isNegative && other.isNegative) {
			return true;
		}
		return signlessGreaterThan(other);
    }

        public boolean signlessGreaterThan(BigInteger other) {
        int i;
        if (this.equalsTo(other)) {
			return false;
		}
        if (values.size() > other.values.size()) {
			return true;
		} else if (values.size() < other.values.size()) {
			return false;
		} else {
            for (i=values.size() - 1; i >= 0; i--) {
				if (values.get(i) > other.values.get(i)) {
					return true;
				} else if(values.get(i) != other.values.get(i)) {
                    return false;
                }
			}
		}
		return false;
    }

    public boolean lessThan(BigInteger other) {
        return other.greaterThan(this);
    }
}
