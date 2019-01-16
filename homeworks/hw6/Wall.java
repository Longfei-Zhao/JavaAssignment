package trump;

import java.util.*;
import java.util.stream.*;

public class Wall{
	
	private Brick[][] bricks;
	private int width;
	private int height;
	private int lastFilledX;
	private int lastFilledY;
	private boolean isComplete;
	
	final String sideBrick = "\u2b1b";
	final String innerBrick = " \u2b1b"; // black box (brick) in Unicode
	
	public Wall(int w, int h) {
		assert w > 0 && h > 0: "the wall must have finite width and height";
		this.width = w;
		this.height = h;
		this.bricks = new Brick[width][height];
		for (int j = 0; j < this.height; j++)
			for(int i = 0; i < this.width; i++)
				this.bricks[i][j] = null;
		this.lastFilledX = 0;
		this.lastFilledY = 0;
		// this.isComplete = false;
	}
	
	public void lay(Brick brick) {
		if (this.isComplete()) return;
		this.bricks[lastFilledX][lastFilledY] = brick;
		// if (this.isComplete()) return false;
		if (this.lastFilledX == this.width - 1) {
			this.lastFilledX = 0;
			this.lastFilledY += 1;
		} else {
			this.lastFilledX += 1;
		}
	}
	
	public boolean isComplete() {
		return Stream.of(this.bricks).allMatch(
			level -> Stream.of(level).allMatch(b -> b != null));
		// return (this.lastFilledX == this.width - 1 &&
		// 		this.lastFilledY == this.height - 1);
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		for (int j = this.height - 1; j >= 0; j--) {
			for(int i = 0; i < this.width; i++) 
				// try any from the range u25a2 -- u25a9
				buffer.append(bricks[i][j] == null ? "   " : 
				i == 0 ? sideBrick : innerBrick); 
				// buffer.append(bricks[i][j] == 0 ? "0" : "1");
			buffer.append("\n");
		}
		// return "\033[31m" + buffer.toString() + "\033[0m";
		return buffer.toString(); // to hell with color code sequence
	}
	
	public static Wall linkTwoWalls(Wall w1, Wall w2) {
		assert w1.height == w2.height: "Walls have unequal height";
		if (!w1.isComplete() || !w2.isComplete())
			return null; //Optional.empty();
		int w = w1.width + w2.width;
		int h = w1.height;
		Brick[][] bricks = new Brick[w][h];
		System.arraycopy(w1, 0, bricks, 0, w1.width);
		System.arraycopy(w2, w1.width, bricks, 0, w2.width);
		Wall result = new Wall(w,h);
		result.bricks = bricks;
		return result;//Optional.of(result);
	}
	
	public static Optional<Wall> joinWalls(Wall... walls) {
		if (walls == null || walls.length == 0)
			return Optional.empty();
		// check all walls are of the same height
		int firstHeight = walls[0].height;
		Stream<Wall> wallStream = Stream.of(walls);
		assert wallStream.allMatch(w -> w.height == firstHeight);
		return wallStream.reduce((w1,w2) -> linkTwoWalls(w1,w2));
	}
	
	public static void main(String[] args) {
		Wall wall = new Wall(40,10);
		for (int i = 0; i < 411; i++) {
			wall.lay(new Brick(new Ball(10.0)));
			if (wall.isComplete())
				break;
		}
		System.out.print(wall);
		// System.out.println();
		// System.out.printf("width %d and width %d%n",
		// 	wall.bricks.length, wall.width);
		// List<Integer> chars = Stream.of(wall.bricks[0])
		// 	// Arrays.asList(wall.bricks[0]).stream()
		// 	.map(Integer::new)//i -> i == 0 ? 'o' : 'x')
		// 	.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
		// System.out.println(chars);
	}
}
