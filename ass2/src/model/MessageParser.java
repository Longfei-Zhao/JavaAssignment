package model;

import model.Message.Status;
import model.Message.Topic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created with IntelliJ IDEA.
 * User: abx
 * Date: 17/4/17
 * Time: 11:58 PM
 * Created for ass2 in package model
 * Simple JSON parser for documents with flat structure
 * (no json-objects as values); format-stable (ignores any white space)
 */
public class MessageParser {


    /**
     * Gets message from a json-string and marking each with a timestamp
     *
     * @param jsonString the json string
     * @param timeStamp  the time stamp
     * @return the message
     */
    public static Message getMessage(String jsonString, double timeStamp) {
        Map<String, String> jsonMap = getJsonMap(jsonString);
        String text = jsonMap.get("text");
        Topic topic = Topic.valueOf(jsonMap.get("topic").toUpperCase());
        Status status = Status.valueOf(jsonMap.get("status").toUpperCase());

        return new Message(timeStamp, text, topic, status);
    }


    private static List<String> jsonChunks(String js) {
        js = js.trim();
        if (js.startsWith("{")) js = js.substring(1);
        if (js.endsWith("}")) js = js.substring(0,js.length()-1);
        List<String> res = new ArrayList<>();
        int current = 0;
        for (Integer integer : getSeparatorIndices(js)) {
            res.add(js.substring(current, integer));
            current = integer + 1;
        }
        res.add(js.substring(current)); // after the last ','
        return res;
    }

    private static List<Integer> getSeparatorIndices(String js) {
        List<Integer> res = new ArrayList<>();
        short insideBrackets = 0;
        boolean insideDoubleQuotes = false;
        char c;
        for (int i = 0; i < js.length(); i++) {
            c = js.charAt(i);
            if (c == '"') insideDoubleQuotes = !insideDoubleQuotes;
            insideBrackets += changeNestingLevel(c);
            if (c == ',' && insideBrackets == 0 && !insideDoubleQuotes)
                res.add(i);
        }
        return res;
    }

    private static short changeNestingLevel(char c) {
        if (c == '{' || c == '[') return 1;
        if (c == '}' || c == ']') return -1;
        return 0;
    }

    private static Map<String, String> getJsonMap(String js) {
        List<String> pairs = jsonChunks(js);
        Map<String,String> jsonMap = new TreeMap<>();
        String key, value;
        for (String pair: pairs) {
            key = pair.split(":")[0].trim();
            value = pair.split(":",2)[1].trim();
            if (key.startsWith("\"")) key = key.substring(1);
            if (key.endsWith("\"")) key = key.substring(0,key.length()-1);
            if (value.startsWith("\"")) value = value.substring(1);
            if (value.endsWith("\"")) value = value.substring(0,value.length()-1);
            jsonMap.put(key, value);
        }
        return jsonMap;
    }

}
