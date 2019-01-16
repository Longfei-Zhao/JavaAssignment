package splotch;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SceneBuilder;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

/**
 *
 * @author abx
 */
public class Smoother extends Application {

    Path onePath = new Path();
    Path twoPath = new Path();
    ArrayList<Point> points = new ArrayList<Point>();
    Splotch splotch;
    Point2D anchorPt;
    Point currentPoint;
	Point lastPoint;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Draw a shape by dragging, then press 's', then 'm'");

        final Group root = new Group();
        // add paths
        root.getChildren().addAll(onePath, twoPath);

        final Scene scene = 
			SceneBuilder.create()
            			.root(root)
						.width(700)
                		.height(550)
						.fill(Color.WHITE)
                		.build();

        // starting initial path
        scene.onMousePressedProperty().set(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent event) {
                // clear path
                onePath.getElements().clear();
                onePath.setFill(null);
                twoPath.getElements().clear();
                twoPath.setFill(null);
                points.clear();
                // start point in path
                anchorPt = new Point2D(event.getX(), event.getY());
                onePath.setStrokeWidth(3);
                onePath.setStrokeDashOffset(0.7);
                onePath.setStroke(Color.BLACK);//LIGHTGREY);
                onePath.getElements()
                        .add(new MoveTo(anchorPt.getX(), anchorPt.getY()));
                points.add(Point.makePoint(anchorPt.getX(), anchorPt.getY()));
            }
        });

        // dragging creates lineTos added to the path
        scene.onMouseDraggedProperty().set(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent event) {
                currentPoint = Point.makePoint(event.getX(), event.getY());
                points.add(currentPoint);
                onePath.getElements()
                        .add(new LineTo(currentPoint.x, currentPoint.y));
            }
        });

        // end the path when mouse released event
        scene.onMouseReleasedProperty().set(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent event) {
				lastPoint = Point.makePoint(event.getX(), event.getY());
				points.add(lastPoint);
                onePath.getElements()
                        .add(new LineTo(lastPoint.x, lastPoint.y));
				onePath.getElements()
						.add(new LineTo(anchorPt.getX(), anchorPt.getY()));
                onePath.setStrokeWidth(1);
                onePath.setFill(Color.DARKGRAY);
				// writing onePath data in the model for later use
                splotch = new Splotch(points);
            }
        });
        
        scene.onKeyPressedProperty().set(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode() == KeyCode.M) {
                    if (onePath.getElements().size() > 0
                            && twoPath.getElements().size() > 0) {
                        //twoPath.setFill(Color.GRAY);
                        //twoPath.setOpacity(0.5);
                        final Timeline timeline = makeTimeline(onePath, twoPath);
                        timeline.play();
                        System.out.println("Morphing should be seen now");
                    } else {
                        System.out.println("Paths are empty");
                    }
                } else if (ke.getCode() == KeyCode.S) {
                    System.out.println("Attempt to smooth...");
                    //splotch = new Splotch(points);
                    splotch = splotch.smoothSplotch();
                    twoPath.setFill(null);
                    smoothPath(twoPath, splotch);
                    twoPath.setStrokeWidth(1);
                    twoPath.setFill(Color.WHITE);
                    twoPath.setOpacity(0.5);

                }
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void smoothPath(Path path,Splotch sp) {
        path.getElements().clear();
		double x0 = sp.anchorPoint().x;
		double y0 = sp.anchorPoint().y;
        path.getElements().add(new MoveTo(x0, y0));
        for (Point point : sp.points) {
            path.getElements().add(new LineTo(point.x, point.y));
        }
		path.getElements().add(new LineTo(x0, y0));
    }
    
    private Timeline makeTimeline(Path p1, Path p2) {
        final Timeline timeline = new Timeline();
        timeline.setCycleCount(1);//(Timeline.INDEFINITE);
        timeline.setAutoReverse(false);
        int n = onePath.getElements().size();
        KeyValue kvx, kvy;
        KeyFrame kf;
        MoveTo ap1, ap2;
        LineTo pe1, pe2;
        ap1 = (MoveTo) onePath.getElements().get(0);
        ap2 = (MoveTo) twoPath.getElements().get(0);
        kvx = new KeyValue(ap1.xProperty(), ap2.getX());
        kvy = new KeyValue(ap1.yProperty(), ap2.getY());
        kf = new KeyFrame(Duration.millis(5000), kvx, kvy);
        timeline.getKeyFrames().add(kf);
        for (int i = 1; i < n; i++) {
            pe1 = (LineTo) onePath.getElements().get(i);
            pe2 = (LineTo) twoPath.getElements().get(i);
            kvx = new KeyValue(pe1.xProperty(), pe2.getX());
            kvy = new KeyValue(pe1.yProperty(), pe2.getY());
            kf = new KeyFrame(Duration.millis(5000), kvx, kvy);
            timeline.getKeyFrames().add(kf);
        }
        return timeline;
    }
}
