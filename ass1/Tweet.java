import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Created by zhaolongfei on 30/3/17.
 */
public class Tweet {
    public String text;
    public User user;
    public String lang;
    public Coordinates coordinates;
    public ZonedDateTime created_at;
    public int result;//For matching topic. 1 for definitive, 0 for tentative, -1 for reject.

    public class User {
        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public long id;
        public String name;
    }

    public class Coordinates {
        public ArrayList<Double> coordinates;
        public String type;

        public ArrayList<Double> getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(ArrayList<Double> coordinates) {
            this.coordinates = coordinates;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return "Coordinates{" +
                    "coordinates=" + coordinates +
                    '}';
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public ZonedDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("E MMM d HH:mm:ss Z yyyy");
        this.created_at = ZonedDateTime.parse(created_at, dateTimeFormatter);
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public Tweet() {

    }

    @Override
    public String toString() {
        return "Tweet{" +
                "text='" + text + '\'' +
                ", user=" + user +
                ", lang='" + lang + '\'' +
                ", coordinates=" + coordinates +
                ", created_at=" + created_at +
                '}';
    }
}
