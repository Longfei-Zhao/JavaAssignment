package trump;

import java.util.*;
import java.util.stream.*;

public class DonaldTrump {
    
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Need three integer arguments: width height #bricks");
            System.exit(1);
        }
        int width = Integer.parseInt(args[0]);
        int height = Integer.parseInt(args[1]);
		int numberOfBricks = Integer.parseInt(args[2]);
		assert numberOfBricks <= width * height: "Too many bricks";
        System.out.printf("Will build a wall %d wide and %d tall%n", 
            width, height);
        System.out.println(String.join("", Collections.nCopies(width,"==")));
        Wall trumpWall = 
            Stream.generate(() -> new Ball(10.0))
             .filter(b -> b.colour == Ball.Colour.RED)
             .map(Brick::new)
             .limit(numberOfBricks) // try remove apriori limit, Task 3
             .reduce(new Wall(width, height), // replace reduce with collect, Task 3
                (wall, brick) -> {wall.lay(brick); return wall;},
                Wall::linkTwoWalls);

        Wall trumpWall1 = new Wall(width, height);
        Stream.generate(() -> new Ball(10.0))
            .filter(b -> b.colour == Ball.Colour.RED)
            .map(Brick::new)
            .limit(numberOfBricks) // try remove apriori limit, Task 3
            .forEach(s -> trumpWall1.lay(s));

        Wall trumpWall2 =
        Stream.generate(() -> new Ball(10.0))
            .filter(b -> b.colour == Ball.Colour.RED)
            .map(Brick::new)
            .limit(numberOfBricks)
            .collect(() -> new Wall(width, height),
                    (wall, s) -> wall.lay(s),
                    (wall1, wall2) -> Wall.linkTwoWalls(wall1, wall2));

        System.out.println(trumpWall2);
    }    
}
