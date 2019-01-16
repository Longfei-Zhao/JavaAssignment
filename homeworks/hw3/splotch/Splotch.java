package splotch;

import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.PI;
import static java.lang.Math.sqrt;
import static java.lang.Math.atan2;
import static java.lang.Math.signum;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: abx
 * Date: 12/06/13
 * Time: 11:14 PM
 */

public class Splotch {

    public ArrayList<Point> points;

    public Splotch(ArrayList<Point> points) {
        this.points = points;
    }

    public Point anchorPoint() {
        return points.get(0);
    }

    public Splotch smoothSplotch() {
        if (points.size() < 3)
            return this;

        ArrayList<Point> newPoints = new ArrayList<Point>();
        double mx = medX();
        double my = medY();
        double firstX = points.get(0).x - mx;
        double firstY = points.get(0).y - my;
        double theta0 = /*0.5*PI*/ - atan2(firstY, firstX);
        double secondX = points.get(1).x - mx;
        double secondY = points.get(2).y - my;
        int orient = (int)signum(firstX*secondY - firstY*secondX);
        int n = points.size();
        double r = radius();
        double theta;

        int px, py;
        for (int i = 0; i < n; i++) {
            theta = orient * 2*PI*i/n - theta0;
            newPoints.add(Point.makePoint(mx + r*cos(theta),
                                          my + r*sin(theta)));
        }
		// newPoints.add(Point.makePoint(mx + r*cos(theta0), my + r*sin(theta0)));
        //System.out.printf("Splotch radius %.2f%n", r);
        return new Splotch(newPoints);
    }

    private double medX() {
        double x = 0;
        for (Point p: points) {
            x += p.x;
        }
        return x/points.size();
    }

    private double medY() {
        double y = 0;
        for (Point p: points) {
            y += p.y;
        }
        return y/points.size();
    }

    private double area() {
        double res = 0;
        if (points.size() < 3)
            return res;
        double cx = medX();
        double cy = medY();
        double dx1, dx2, dy1, dy2;
        for (int i = 0; i < points.size()-1; i++) {
            dx1 = points.get(i).x - cx;
            dy1 = points.get(i).y - cy;
            dx2 = points.get(i+1).x - cx;
            dy2 = points.get(i+1).y - cy;
            res += 0.5 * abs(dx1*dy2 - dx2*dy1);
        }
        dx1 = points.get(points.size()-1).x -cx;
        dy1 = points.get(points.size()-1).y -cy;
        dx2 = points.get(0).x -cx;
        dy2 = points.get(0).y -cy;
        res += 0.5 * abs(dx1*dy2 - dx2*dy1);

        return res;
    }

    private double radius() {
        return sqrt(area()/PI);
    }
}
