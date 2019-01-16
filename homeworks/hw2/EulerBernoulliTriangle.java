'''Java offers BigInteger to solve big number problem.
I think it uses string to present number to operate.

Replace all long by BigInteger.
Replace new long by new BigInteger().
Use the funcion add() that BigInteger offers.
'''

import java.math.BigInteger;
public class EulerBernoulliTriangle {

	private int height;
	private BigInteger[][] elems;

	public EulerBernoulliTriangle(int h) {
		this.height = h;
		initialize();
		//printTriangle();
		System.out.println("The Euler numbers:\n" + getEulerNumbers());
		System.out.println("The Bernoulli numbers:\n" + getBernoulliNumbers());
	}

	private void initialize() {
		elems = new BigInteger[height][];
		elems[0] = new BigInteger[1];
		elems[0][0] = new BigInteger("1");
		int lodd, leven;
		for (int i = 1; i < height; i+=2) {
			lodd = i+1;
			leven = i+2;
			elems[i] = new BigInteger[lodd];
			elems[i+1] = new BigInteger[leven];
			elems[i][0] = new BigInteger("0");
			elems[i+1][leven - 1] = new BigInteger("0");
			for (int j = 1; j < lodd; j++) {
				elems[i][j] = elems[i-1][j-1].add(elems[i][j-1]);
			}
			for (int j = leven - 2; j >= 0; j--) {
				elems[i+1][j] = elems[i+1][j+1].add(elems[i][j]);
			}
		}
	}

	public void printTriangle() {
		for (int i = 0; i < elems.length; i++) {
			for (int j = 0; j < elems[i].length; j++)
				System.out.print(elems[i][j] + " ");
			System.out.println();
		}
	}

	public BigInteger[] euler() {
		BigInteger[] left = new BigInteger[(height-1) / 2];
		left[0] = new BigInteger("1");
		for (int i = 1; i<left.length; i++)
			left[i] = elems[2*i][0];
		return left;
	}

	public String getEulerNumbers() {
		String s = "";
		for(BigInteger eul : euler())
			s += eul + "\n";
		return s + "That\'s " + euler().length + " Euler numbers\n" ;
	}

	public BigInteger[] bernoulli() {
		BigInteger[] right = new BigInteger[height / 2];
		right[0] = new BigInteger("1");
		for (int i = 1; i<right.length; i++)
			right[i] = elems[2*i - 1][2*i - 1];
		return right;
	}

	public String getBernoulliNumbers() {
		String s = "";
		for(BigInteger ber : bernoulli())
			s += ber + "\n";
		return s + "That\'s " + bernoulli().length + " Bernoulli numbers\n" ;
	}

	public static void main(String[] args) {
		assert args.length > 0;
		int i = 0;
		try {
			i = Integer.parseInt(args[0]);
		} catch (NumberFormatException nfe) {
			System.out.println(nfe.toString() + " must be odd integer");
		} finally {
			if (i%2 != 1 || i == 1) {
				System.out.println("Enter an odd number greater than 2 only");
				System.exit(1);
			}
			System.out.println("The height of the triangle is " + i);
		}

		EulerBernoulliTriangle triangle = new EulerBernoulliTriangle(i);
	}
}
