import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Created by zhaolongfei on 13/4/17.
 */
public class FinalOutput {
    public String topicType;
    public int numberOfDefinitives;
    public int numberOfTentatives;
    public Cluster cluster;
    public String begin;
    public String end;

    public class Cluster {
        public boolean found = true;
        public ArrayList<Double> topLeft;
        public ArrayList<Double> bottomRight;

        public boolean isFound() {
            return found;
        }

        public void setFound(boolean found) {
            this.found = found;
        }

        public ArrayList<Double> getTopLeft() {
            return topLeft;
        }

        public void setTopLeft(ArrayList<Double> topLeft) {
            this.topLeft = topLeft;
        }

        public ArrayList<Double> getBottomRight() {
            return bottomRight;
        }

        public void setBottomRight(ArrayList<Double> bottomRight) {
            this.bottomRight = bottomRight;
        }

        public Cluster(ArrayList<Double> topLeft, ArrayList<Double> bottomRight) {
            this.topLeft = topLeft;
            this.bottomRight = bottomRight;
        }
    }

    public String getTopicType() {
        return topicType;
    }

    public void setTopicType(String topicType) {
        this.topicType = topicType;
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

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public FinalOutput(Event event) {
        this.topicType = event.getTopic().getTopicName();
        this.numberOfDefinitives = event.getNumberOfDefinitives();
        this.numberOfTentatives = event.getNumberOfTentatives();
        this.cluster = new Cluster(event.getBoundingbox().getMinCoordinates(), event.getBoundingbox().getMaxCoordinates());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("E MMM d HH:mm:ss Z yyyy");
        this.begin = event.getStartTime().format(dateTimeFormatter);
        this.end = event.getEndTime().format(dateTimeFormatter);
    }

}
