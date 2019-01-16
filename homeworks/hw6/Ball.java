package trump;

import java.util.Random;
import java.util.stream.*;

class Ball {
	public enum Colour {
		RED(1), YELLOW(2), GRAY(3);
		private final int ccode;
		Colour(int i) { this.ccode = i;}
	}
	
	private static Random rand = new Random();
	private final int numColours = Colour.values().length;
	final double radius;
	final Colour colour;
	
	public Ball(double rad) {
		this.colour = Colour.values()[rand.nextInt(numColours)];
		this.radius = rad;
	}
	
	@Override
	public String toString() {
		return String.format("%s ball of radius %.2f", 
			this.colour, this.radius);
	}
	
	public static void main(String[] args) {
		Stream.generate(() -> new Ball(10.0))
			.limit(20)
			.forEach(System.out::println);
	}
}
