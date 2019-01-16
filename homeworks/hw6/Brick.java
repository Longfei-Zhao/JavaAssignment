package trump;

import static java.lang.Math.PI;
import static java.lang.Math.sqrt;
import java.util.Optional;
import java.util.stream.*;

class Brick {
    
    final Optional<Integer> side;
    final Ball.Colour colour;
    
    public Brick(Ball ball) {
        this.side = Optional.of((int)sqrt(PI * ball.radius * ball.radius));
        this.colour = ball.colour;      
    }
    
    @Override
    public String toString() {
        return String.format("%s squared brick of size %d",
                    this.colour, this.side.orElse(Integer.valueOf(0)));
    }
    
    public static void main(String[] args) {
        Stream.generate(() -> new Ball(10.0))
              .limit(20)
              // .peek(System.out::println)
              .filter(b -> b.colour == Ball.Colour.RED)
              .map(Brick::new)
              .forEach(System.out::println);
    }
}
