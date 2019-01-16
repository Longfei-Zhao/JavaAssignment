import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * This TwitterAnalyser.java get all topics and all tweets.
 * Then, match tweets with topics to get useful tweets.
 * Next, analyse those tweets and get the events and group those events.
 * Finally, print events with Json format string.
 * @author Longfei Zhao
 *
 * */
public class TwitterAnalyser {

    public static void main(String[] args) throws Exception {
        assert args != null & args.length > 0;

        List<Topic> topics = getTopics();
        int n = chooseTopic(topics);
        List<Tweet> allTweets = getAllTweets(args[0]);
        List<Event> events = new ArrayList<>();

        for (Topic topic : topics) {
            //if n = 0, we need to analyse all topic, else we just need to analyse the topic user choose.
            if (n != 0 && topic.getId() != n) {
                continue;
            }
            List<Tweet> tweets = getUsefulTweets(topic, topics, allTweets);
            events.addAll(analyse(tweets, topic));
        }
        output(events);
    }

    /**
     * Read the file topics.properties.
     * Analyse it and get all topics information.
     * Refer to ShowProperties.java.
     * @return return a topics list, include all topics information -- topicName, fileName, id, keywords.
     * */
    private static List<Topic> getTopics() throws IOException {

        String topicsPath = "./topics.properties";
        Path path = Paths.get(topicsPath);
        Path parentDir = path.getParent();
        Properties props = new Properties();
        props.load(new FileReader(new File(topicsPath)));
        List<Topic> topics = new ArrayList<>();
        props.forEach((k,v) -> {
            Topic topic = new Topic(k.toString(), v.toString());
            try {
                Files.lines(Paths.get(String.format("%s/%s",parentDir,v)))
                        .forEach(s -> {
                            topic.rules.add(s);
                        });
                topics.add(topic);
            } catch (IOException ioe) {
                System.out.printf("The keyword set %s cannot be open%s", ioe);
            }
        });
        return topics;
    }

    /**
     * Get the input from console.
     * Check whether its format is correct.
     * @return the number that user input.
     * */
    private static int chooseTopic(List<Topic> topics) throws Exception {
        System.out.printf("The following topics can be used in search:\n\n0. all topics\n");
        for (Topic topic : topics){
            System.out.printf("%d. %s\n", topic.getId(), topic.getTopicName());
        }
        System.out.printf("...\n\nChoose by number (empty to quit):\n");
        Scanner reader = new Scanner(System.in);
        String input = reader.nextLine();
        int n = 0;
        if (input.length() == 0) {
            System.exit(0);
        }
        else {
            n = Integer.parseInt(input);
            if (n < 0 || n > Topic.getAmount()) {
                throw new Exception("Input Error!");
            }
            else if(n == 0) {
                System.out.println("User chose all topics");
            }
            else {
                for (Topic topicTemp : topics) {
                    if (topicTemp.getId() == n) {
                        System.out.println("User chose " + topicTemp.getTopicName());
                    }
                }
            }
        }
        return n;
    }

    /**
     * Read the tweets from file.
     * Refer to JacksonSamplerStream.java.
     * @return return a tweet list, include all tweets information -- text, user, lang, coordinates, created_at, result(null for match topic)
     * */
    private static List<Tweet> getAllTweets(String str) throws IOException {
        List<Tweet> tweets = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        Stream<String> lines = Files.lines(Paths.get(str),
                StandardCharsets.UTF_8);
        lines.forEach(s -> {
            try {
                Tweet tweet = mapper.readValue(s, Tweet.class);
                tweets.add(tweet);
            } catch (IOException ioe) {
                System.out.printf("Bad json record %s%n", ioe);
            }
        });
        return tweets;
    }

    /**
     * Match tweets with topics.
     * Print all tweets that match the topics.
     * Save tweets that have location information.
     * @param topic Match tweet with this topic.
     * @param topics all topics.
     * @param allTweets all tweets.
     * @return return a tweet list that have location information for later grouping.
     * */
    private static List<Tweet> getUsefulTweets(Topic topic, List<Topic> topics, List<Tweet> allTweets) {
        List<Tweet> tweets = new ArrayList<>();
        System.out.println("Tweets matching topic " + topic.getTopicName());
        for (Tweet tweet : allTweets) {
            int resultMatchTopic = matchTopic(topic.getId(), tweet, topics);
            if (resultMatchTopic >= 0) {
                if (tweet.getCoordinates() == null) {
                    System.out.println("userId: \"" + tweet.getUser().getId() + "\", userName: \"" + tweet.getUser().getName() + "\", timestamp: \"" + tweet.getCreated_at() + "\"");
                }
                else {
                    System.out.println("userId: \"" + tweet.getUser().getId() + "\", userName: \"" + tweet.getUser().getName() + "\", coordinates: \"" + tweet.getCoordinates().getCoordinates() + "\", timestamp: \"" + tweet.getCreated_at() + "\"");
                    tweet.setResult(resultMatchTopic);
                    tweets.add(tweet);
                }
            }
        }
        return tweets;
    }

    /**
     * Calculation details - Match Topics.
     *
     * @return return the result of matchTopic, 1 for Definitive, 0 for Tentative, -1 for reject.
     * */
    private static int matchTopic(int n, Tweet tweet, List<Topic> topics) {
        boolean hasKey = false;
        boolean hasSharedKey = false;
        List<String> positiveKeywords = new ArrayList<>();
        List<String> negativeKeywords = new ArrayList<>();
        int difference = 0;
        String[] words = tweet.getText().split("\\s+");
        for (String word : words) {
            boolean positiveKey = false;
            boolean negativeKey = false;
            for (Topic topic : topics) {
                for (String rule : topic.getRules()) {
                    if (word.toLowerCase().contains(rule)) {
                        if (topic.getId() == n) {
                            if (positiveKeywords.contains(rule)) {
                                continue;
                            }
                            else {
                                positiveKeywords.add(rule);
                                positiveKey = true;
                                hasKey = true;
                            }

                        } else {
                            if (negativeKeywords.contains(rule)) {
                                continue;
                            }
                            else {
                                negativeKeywords.add(rule);
                                negativeKey = true;
                            }
                        }
                    }
                }
            }
            if (positiveKey && negativeKey) {
                hasSharedKey = true;
            }
            if (positiveKey) {
                difference++;
            }
            if (negativeKey) {
                difference--;
            }
        }

        if (hasKey) {
            if (!hasSharedKey || difference > 0) {
                return 1;
            }
            else if ( difference == 0) {
                return 0;
            }
            else {
                return -1;
            }
        }
        else {
            return -1;
        }
    }

    /**
     * Analyse the useful tweets.
     * Get the events.
     * Group those events.
     * Delete invalid events.
     * @return events
     * */
    private static List<Event> analyse(List<Tweet> tweets, Topic topic) {
        List<Event> events = new ArrayList<>();
        for (Tweet tweet : tweets) {
            boolean isFound = false;
            for (Event event : events) {
                ArrayList<Double> coordinates = tweet.getCoordinates().getCoordinates();
                ArrayList<Double> maxCoordinates = event.getBoundingbox().getMaxCoordinates();
                ArrayList<Double> minCoordinates = event.getBoundingbox().getMinCoordinates();
                if (coordinates.get(0) < minCoordinates.get(0) - 0.5
                    || coordinates.get(1) < minCoordinates.get(1) - 0.5
                    || coordinates.get(0) > maxCoordinates.get(0) + 0.5
                    || coordinates.get(1) > maxCoordinates.get(1) + 0.5
                    || tweet.getCreated_at().isAfter(event.getEndTime().plusHours(6))
                    || tweet.getCreated_at().isBefore(event.getStartTime().minusHours(6))) {
                    continue;
                }
                isFound = true;
                ArrayList<Double> newMinCoordinates = new ArrayList<>();
                ArrayList<Double> newMaxCoordinates = new ArrayList<>();
                newMinCoordinates.add(Math.min(coordinates.get(0), minCoordinates.get(0)));
                newMinCoordinates.add(Math.min(coordinates.get(1), minCoordinates.get(1)));
                newMaxCoordinates.add(Math.max(coordinates.get(0), maxCoordinates.get(0)));
                newMaxCoordinates.add(Math.max(coordinates.get(1), maxCoordinates.get(1)));
                event.setBoundingbox(new Event.BoundingBox(newMaxCoordinates, newMinCoordinates));
                if (tweet.getCreated_at().isBefore(event.getStartTime())) {
                    event.setStartTime(tweet.getCreated_at());
                }
                if (tweet.getCreated_at().isAfter(event.getEndTime())) {
                    event.setEndTime(tweet.getCreated_at());
                }
                if (tweet.getResult() == 0) {
                    event.setNumberOfTentatives(event.getNumberOfTentatives() + 1);
                }
                if (tweet.getResult() > 0) {
                    event.setNumberOfDefinitives(event.getNumberOfDefinitives() + 1);
                }
                break;
            }
            if (!isFound) {
                if (tweet.getResult() == 0){
                    events.add(new Event(topic,
                            0,
                            1,
                            new Event.BoundingBox(tweet.getCoordinates().getCoordinates(), tweet.getCoordinates().getCoordinates()),
                            tweet.getCreated_at(),
                            tweet.getCreated_at()));
                }
                else {
                    assert tweet.getResult() == 1;
                    events.add(new Event(topic,
                            1,
                            0,
                            new Event.BoundingBox(tweet.getCoordinates().getCoordinates(), tweet.getCoordinates().getCoordinates()),
                            tweet.getCreated_at(),
                            tweet.getCreated_at()));
                }

            }
        }
        List<Event> resEvents;
        while (true) {
            resEvents = new ArrayList<>();
            for (Event event : events) {
                boolean isFound = false;
                for (Event resEvent : resEvents) {
                    if (event.getBoundingbox().getMinCoordinates().get(0) > resEvent.getBoundingbox().getMaxCoordinates().get(0) + 0.5
                        || event.getBoundingbox().getMinCoordinates().get(1) > resEvent.getBoundingbox().getMaxCoordinates().get(1) + 0.5
                        || event.getBoundingbox().getMaxCoordinates().get(0) < resEvent.getBoundingbox().getMinCoordinates().get(0) - 0.5
                        || event.getBoundingbox().getMaxCoordinates().get(1) < resEvent.getBoundingbox().getMinCoordinates().get(1) - 0.5
                        || event.getStartTime().isAfter(resEvent.getEndTime().plusHours(6))
                        || event.getEndTime().isBefore(resEvent.getStartTime().minusHours(6))) {
                        continue;
                    }
                    isFound = true;
                    ArrayList<Double> newMinCoordinates = new ArrayList<>();
                    ArrayList<Double> newMaxCoordinates = new ArrayList<>();
                    newMinCoordinates.add(Math.min(event.getBoundingbox().getMinCoordinates().get(0), resEvent.getBoundingbox().getMinCoordinates().get(0)));
                    newMinCoordinates.add(Math.min(event.getBoundingbox().getMinCoordinates().get(1), resEvent.getBoundingbox().getMinCoordinates().get(1)));
                    newMaxCoordinates.add(Math.max(event.getBoundingbox().getMaxCoordinates().get(0), resEvent.getBoundingbox().getMaxCoordinates().get(0)));
                    newMaxCoordinates.add(Math.max(event.getBoundingbox().getMaxCoordinates().get(1), resEvent.getBoundingbox().getMaxCoordinates().get(1)));
                    resEvent.setBoundingbox(new Event.BoundingBox(newMaxCoordinates, newMinCoordinates));

                    if (event.getStartTime().isBefore(resEvent.getStartTime())) {
                        resEvent.setStartTime(event.getStartTime());
                    }
                    if (event.getEndTime().isAfter(resEvent.getEndTime())) {
                        resEvent.setEndTime(event.getEndTime());
                    }
                    resEvent.setNumberOfDefinitives(resEvent.getNumberOfDefinitives() + event.getNumberOfDefinitives());
                    resEvent.setNumberOfTentatives(resEvent.getNumberOfTentatives() + event.getNumberOfTentatives());
                    break;
                }
                if (!isFound) {
                    resEvents.add(event);
                }
            }
            if (resEvents.size() == events.size()) {
                break;
            }
            events = new ArrayList<>(resEvents);
        }
        List<Event> res = new ArrayList<>();
        for (Event resEvent : resEvents) {
            if (resEvent.getNumberOfDefinitives() != 0) {
                res.add(resEvent);
            }
        }
        return res;
    }

    /**
     * Print the size of events.
     * Object to json(String).
     * Print those String.
     * */
    private static void output(List<Event> events) throws JsonProcessingException {
        System.out.println("found " + events.size() + " groups");
        ObjectMapper mapper = new ObjectMapper() ;

        for (Event event : events) {
            FinalOutput finalOutput = new FinalOutput(event);
            String jsonInString = mapper.writeValueAsString(finalOutput);
            System.out.println(jsonInString);
        }
    }

}