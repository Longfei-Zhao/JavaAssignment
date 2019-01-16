import java.time.ZonedDateTime;
import java.util.ArrayList;

/**
 * Created by zhaolongfei on 30/3/17.
 */
public class Event {
    private Topic topic;
    private int numberOfDefinitives;
    private int numberOfTentatives;
    private BoundingBox boundingbox;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;

    public static class BoundingBox {
        private ArrayList<Double> maxCoordinates;
        private ArrayList<Double> minCoordinates;

        public ArrayList<Double> getMaxCoordinates() {
            return maxCoordinates;
        }

        public void setMaxCoordinates(ArrayList<Double> maxCoordinates) {
            this.maxCoordinates = maxCoordinates;
        }

        public ArrayList<Double> getMinCoordinates() {
            return minCoordinates;
        }

        public void setMinCoordinates(ArrayList<Double> minCoordinates) {
            this.minCoordinates = minCoordinates;
        }

        public BoundingBox(ArrayList<Double> maxCoordinates, ArrayList<Double> minCoordinates) {
            this.maxCoordinates = maxCoordinates;
            this.minCoordinates = minCoordinates;
        }
    }


    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public int getNumberOfDefinitives() {
        return numberOfDefinitives;
    }

    public void setNumberOfDefinitives(int numberOfDefinitives) {
        this.numberOfDefinitives = numberOfDefinitives;
    }

    public int getNumberOfTentatives() {
        return numberOfTentatives;
    }

    public void setNumberOfTentatives(int numberOfTentatives) {
        this.numberOfTentatives = numberOfTentatives;
    }

    public BoundingBox getBoundingbox() {
        return boundingbox;
    }

    public void setBoundingbox(BoundingBox boundingbox) {
        this.boundingbox = boundingbox;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public Event(Topic topic, int numberOfDefinitives, int numberOfTentatives, BoundingBox boundingbox, ZonedDateTime startTime, ZonedDateTime endTime) {
        this.topic = topic;
        this.numberOfDefinitives = numberOfDefinitives;
        this.numberOfTentatives = numberOfTentatives;
        this.boundingbox = boundingbox;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
