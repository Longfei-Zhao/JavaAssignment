package model;

/**
 * Created with IntelliJ IDEA.
 * User: abx
 * Date: 18/4/17
 * Time: 12:11 AM
 * Created for ass2 in package model
 *
 * Class to represent a data set (content and metadata)
 * of a news piece. The timeStamp marks the position of the message
 * in the running message queue, the topic allows to filter (select)
 * the messages which are displayed, the status (not yet put to use)
 * allows to implement a priority ordering in the message queue based
 * on both the timestamp and the status value (hence we use PriorityQueue
 * for the message buffer in Feeder.
 */

//Add CompareTo()
public class Message implements Comparable<Message> {

    public final double timeStamp;
    public final String text;
    public final Topic topic;
    public final Status status;

    public Message(double timeStamp, String text, Topic topic, Status status) {
        this.timeStamp = timeStamp;
        this.text = text;
        this.topic = topic;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Message{" +
                "timeStamp=" + timeStamp +
                ", text='" + text + '\'' +
                ", topic=" + topic +
                ", status=" + status +
                '}';
    }

    @Override
    public int compareTo(Message o) {
        return o.status.value() - status.value();
    }

    public enum Topic {
        WEATHER, BUSINESS,
        INTERNATIONAL,
        DOMESTIC, SCITECH,
        SPORT, MISCELLANEOUS,// UNKNOWN
    }

    public enum Status {
        EMERGENCY(10),
        BREAKING_NEWS(5),
        NORMAL(3),
        SPECIAL_INTEREST(1);// ,UNKNOWN(0);

        private final int value;

        Status(int i) {
            value = i;
        }

        public int value() {
            return value;
        }
    }
}
