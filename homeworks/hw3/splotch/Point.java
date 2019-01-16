package splotch;

/**
 * Created with IntelliJ IDEA.
 * User: abx
 * Date: 12/06/13
 * Time: 11:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class Point {

    public final double x, y;

    private Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Point makePoint(double x, double y) {
        return new Point(x, y);
    }
}
