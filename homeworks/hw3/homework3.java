public class homework3 {
	public static void main( String[] args ) {
		long number = Long.parseLong(args[0]);//Get the input
		double sqrt = Math.sqrt(number);//Calculate the square root
		//because if number have a divisor not itself, it must small than the square root
		System.out.printf("%d = 1", number);//print the start of the output
		int p = 2;//initialize the divisor

		while(true){//start loop to find the smalleset divisor for current number
			if (number == 1) {//if number == 1, should break loop.
				System.out.printf("\n");
				break;
			}
			if (number % p == 0) {//if p is a divisor, print p and then number := number / p
				System.out.printf(" * %d", p);
				number = number / p;
				sqrt = Math.sqrt(number);//get the square root of the new number
				p = 2;
				continue;
			}
			p++;
			if(p > sqrt){//if p > sqrt, the current number don't have a divisor, so break loop
				System.out.printf(" * %d\n", number);
				break;
			}
		}
	}
}
