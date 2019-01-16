import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaolongfei on 30/3/17.
 */
public class Topic {
    private String topicName;
    private String fileName;
    private int id;
    static int nextid;
    public List<String> rules;

    static {
        nextid = 1;
    }

    public static int getAmount() {
        return nextid - 1;
    }

    public Topic() {
    }

    public Topic(String topicName, String fileName) {
        this.topicName = topicName;
        this.fileName = fileName;
        this.id = nextid;
        nextid++;
        this.rules = new ArrayList<String>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public List<String> getRules() {
        return rules;
    }

    public void setRules(List<String> rules) {
        this.rules = rules;
    }

    @Override
    public String toString() {
        return "Topic{" +
                "topicName='" + topicName + '\'' +
                ", fileName='" + fileName + '\'' +
                ", id=" + id +
                ", rules=" + rules +
                '}';
    }
}
