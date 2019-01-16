/**
 * Created by zhaolongfei on 30/4/17.
 */
public class Bottle implements Comparable<Bottle> {
    public Beer beer;
    public double volume;
    public double alcoholContent;
    public enum GlassColour {RED, BLACK, GREEN, WHITE};
    public GlassColour glassColour;
    public double price;
    public int quantity;

    public static class Beer {
        public String brandName;
        public double strength;

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

        public double getStrength() {
            return strength;
        }

        public void setStrength(double strength) {
            this.strength = strength;
        }

        public Beer(String brandName, double strength) {
            this.brandName = brandName;
            this.strength = strength;
        }

        @Override
        public String toString() {
            return "Beer{" +
                    "brandName='" + brandName + '\'' +
                    ", strength=" + strength +
                    '}';
        }
    }

    public Beer getBeer() {
        return beer;
    }

    public void setBeer(Beer beer) {
        this.beer = beer;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getAlcoholContent() {
        return alcoholContent;
    }

    public void setAlcoholContent() {
        this.alcoholContent = this.getBeer().getStrength() * this.getVolume();
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Bottle(Beer beer, double volume, GlassColour glassColour, double price, int quantity) {
        this.beer = beer;
        this.volume = volume;
        this.glassColour = glassColour;
        this.price = price;
        this.quantity = quantity;
        this.setAlcoholContent();
    }

    @Override
    public String toString() {
        return "Bottle{" +
                "beer=" + beer +
                ", volume=" + volume +
                ", alcoholContent=" + alcoholContent +
                ", glassColour=" + glassColour +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }

    @Override
    public int compareTo(Bottle bottle) {
        return (int) (this.getPrice() - bottle.getPrice());
    }
}
