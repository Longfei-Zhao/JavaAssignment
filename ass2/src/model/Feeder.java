package model;

import model.Message.Topic;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: abx
 * Date: 15/4/17
 * Time: 1:33 AM
 * Created for ass2 in package model
 */

public class Feeder {

    // private static final String newsFeederMockFile = "data_stream.txt";
    // bufferFileName is to be removed in rpoper GUI version
    private static final String bufferFileName = "message_buffer.txt";
    private static final int origin = 2; // shortest time in sec between two feeds
    private static final int bound = 5; // longest time in sec between two feeds
    // queue to contain the text messages, later may become PriorityQueue
	private Queue<Message> buffer; 
    //Change to PriorityQueue
    public Feeder() {
        this.buffer = new PriorityQueue<>();
    }

    public Queue<Message> getBuffer() {
        return this.buffer;
    }

    /**
     * reads lines from the data file, parses each line and creats a Message
     * object with the timestamp generated randomly; the messages are placed
     * in the buffer in the chronological order. Later changes to implement
     * priority ordering (based both on the timestamp and the status) may
     * affect the message order
     * 
     * @param feedSource is the path to the data source file
     */
	public void fillNewsBuffer(Path feedSource)
            throws IOException {
        double tt[] = new double[]{0};
        double dt[] = new double[1];
        Random random = new Random();

        Files.lines(feedSource, StandardCharsets.UTF_8)
                .map(s -> {
                    dt[0] = (bound - origin) * random.nextDouble() + origin;
                    tt[0] += dt[0];
                    return MessageParser.getMessage(s, tt[0]);
                })
                .forEach(buffer::add);
    }

    /**
     * filters out the messages from the original buffer to retain
     * only those whose topics are fount in the topics set
     * 
     * @param topics the set of topics for all fitlered messages
     * to belong to
     */
	public Queue<Message> filterNewsBuffer(Set<Topic> topics) {
        return new ArrayDeque<>(buffer.stream()
                .filter(m -> topics.contains(m.topic))
                .collect(Collectors.toList()));
    }


    /**
     * auxiliary method to "animate" the messages when printed on the
     * command line (not in the GUI window); just run "make runcli" to
     * see it in action, no actual use in the GUI version of the programm
     */
	public void printBufferSlowly() {
        this.buffer.forEach(m -> {
            try {
                printSlowly(m.text, System.out);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

    }

    private static void printSlowly(CharSequence text, PrintStream out)
            throws InterruptedException {
        System.out.println();
        for (int i = 0; i < text.length(); i++) {
            Thread.sleep(75);
            out.print(text.charAt(i));
        }
        out.println();
    }

    public static void main(String[] args) throws IOException {
        Feeder feeder = new Feeder();
        feeder.fillNewsBuffer(Paths.get(args[0]));
        feeder.printBufferSlowly();
//        feeder.getBuffer().forEach(System.out::println);
    }


}
