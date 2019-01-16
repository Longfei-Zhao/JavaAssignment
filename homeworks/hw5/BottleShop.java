import java.util.*;

/**
 * Created by zhaolongfei on 30/4/17.
 */
public class BottleShop{
    public List<Bottle> bottles;

    public List<Bottle> getBottles() {
        return bottles;
    }

    public void setBottles(List<Bottle> bottles) {
        this.bottles = bottles;
    }

    public BottleShop(List<Bottle> bottles) {
        this.bottles = bottles;
    }

    public int calNumOfBrand(String brand) {
        int sum = 0;
        for (Bottle bottle : this.getBottles()) {
            if (bottle.getBeer().getBrandName() == brand) {
                sum += bottle.getQuantity();
            }
        }
        return sum;
    }

    public double sumOfPrice () {
        double sum = 0;
        for (Bottle bottle : this.getBottles()) {
            sum += bottle.price * bottle.quantity;
        }
        return sum;
    }

    public void sortByPrice () {
        Collections.sort(this.getBottles());
    }

    public void print() {
        for (Bottle bottle : this.getBottles()) {
            System.out.println(bottle);
        }
    }

    public static void main(String[] args){
        List<Bottle.Beer> beers = new ArrayList<>();
        List<Bottle> bottles = new ArrayList<>();
        beers.add(new Bottle.Beer("Qingdao", 2.0));
        beers.add(new Bottle.Beer("Yanjing", 3.0));
        beers.add(new Bottle.Beer("Haerbin", 4.0));
        bottles.add(new Bottle(beers.get(0), 200.0, Bottle.GlassColour.RED, 15.0, 5));
        bottles.add(new Bottle(beers.get(1), 300.0, Bottle.GlassColour.BLACK, 10.0, 10));
        bottles.add(new Bottle(beers.get(2), 500.0, Bottle.GlassColour.GREEN, 30.0, 25));
        bottles.add(new Bottle(beers.get(0), 400.0, Bottle.GlassColour.WHITE, 20.0, 15));
        BottleShop bottleShop = new BottleShop(bottles);
        for (Bottle.Beer beer : beers) {
            System.out.println("Sum of " + beer.getBrandName() + ": " + bottleShop.calNumOfBrand(beer.getBrandName()));
        }
        System.out.println("Sum of Price: " + bottleShop.sumOfPrice());

        bottleShop.sortByPrice();
        System.out.println("Sorted By Price:");
        bottleShop.print();
    }

}
