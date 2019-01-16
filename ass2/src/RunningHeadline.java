import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Feeder;
import model.Message;

import javax.management.timer.TimerNotification;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Array;
import java.sql.Time;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.max;
import static java.util.Arrays.asList;
import static model.Message.Topic;

/**
 * Created with IntelliJ IDEA.
 * User: abx
 * Date: 15/4/17
 * Time: 1:20 AM
 * Main class for Ass2 application "Courant-En-Tete" --
 * running headlines. JavaFX Application which
 * should set up a simple scene with a menu bar for file
 * operations, and some controls to operate the running
 * messages -- to pause and resume, speed-up and slow-down,
 * skip to next message, or rewind to previous. Also, a row
 * of control check boxes can be added to control message
 * topic selections to be displayed in the running headlines.
 * One more extension is to implement a priority queue for
 * the message buffer (@see model.Feeder)
 */


public class RunningHeadline extends Application {

    // dimensions of the scene with text
	private final double displayWidth = 750;
    private final double displayHeight = 150;
    private double playSpeed = 0.2; // make larger to play faster
    private Group group; // container for controls and text
    HBox hbox = new HBox();
    private Feeder feeder; // model class which stores and processes messages
    private List<Timeline> timelines = new ArrayList<>(); // animated messages

    //Get the topics from Message
    private List<String> topics = Stream.of(Message.Topic.values())
                                        .map(Message.Topic::name)
                                        .collect(Collectors.toList());
    private List<CheckBox> checkBoxs = new ArrayList<>();

    private File file;
    //For next and previous
    private int indexCurTimeline = 0;

    
	/**
	 * removes any messages from the news buffer and fills it
	 * with news one which are read from a file
	 * @param newsFileName the name of file to open
	 */
	private void setUpFeed(String newsFileName) throws IOException {
            feeder.getBuffer().clear();
            feeder.fillNewsBuffer(Paths.get(newsFileName));
    }

    /**
     * set up messager and delete the 0 - index part message.
     * @param indexCurTimeline delete message from start to indexCurTimeline.
     */
    private Queue<Message> setUpMessager(int indexCurTimeline) {
        Queue<Message> messageBuffer = feeder.getBuffer();
        for (int i = 0; i < indexCurTimeline; i++) {
            messageBuffer.poll();
        }
        return messageBuffer;
    }

    /**
     * given the contents of the message buffer, creates a list
     * of timeline objects with start and end properties for text
     * objects set up, the speed at which each message timeline will
     * be palyed (the same for all -- the messages are like railroad cars
     * in one train chain)
     */
	private void setUpTimeline(int index) {
        double offset = displayWidth + 20; // starting x-pos for message stream
        double playtime = 0;
        Message latestMessage;
        Queue<Message> messageBuffer = setUpMessager(index);
        List<String> topicsAllowed = new ArrayList<>();
        checkBoxs.forEach(checkBox -> {
            if (checkBox.isSelected()) {
                topicsAllowed.add(checkBox.getText());
            }
        });
        while (!messageBuffer.isEmpty()) {
            Timeline timeline = new Timeline();
            latestMessage = messageBuffer.poll();
            if (topicsAllowed.contains(latestMessage.topic.toString())) {
                String messageBody = "\u261E" + latestMessage.text + "\u261C";
                Text text = new Text(offset, displayHeight - 15,
                        messageBody);
                // use another font if this one is not available on your system
                text.setFont(Font.font("Tahoma", FontWeight.BLACK, 80));
                text.setTextOrigin(VPos.BASELINE);

                double mesWidth = text.getLayoutBounds().getWidth();
                playtime += mesWidth / playSpeed;
                setNewsPieceForRun(text, group, timeline, playtime);
                offset += mesWidth;
            }
        }
    }

    /**
     * defines parameters of (message) timeline
     * @param text is a text object which will be animated
     * @param group is the parent container to which the text is added as child
     * @param tl is a timeline for individual animation of this text
     * @param playtime is the time for tl to be played (no autoreverse, 1 cycle)
     * 
     * tl is defined to remove itself from the timelines list when it is 
     * finished (this may need to be removed for features like "play previous"
     * to be implemented); the reason for this removal is to reduce the memory
     * usage mainly. Also, a diagnostic messages is added to be printed when the
     * tl timeline complets (can be removed with no harm)
     */
	private void setNewsPieceForRun(Text text, Group group, 
				Timeline tl, double playtime) {
        group.getChildren().add(text);
        int mesLength = (int) text.getLayoutBounds().getWidth();

        tl.setCycleCount(1);
        tl.setAutoReverse(true);
        KeyValue kv = new KeyValue(text.xProperty(),
                -10 - mesLength, Interpolator.LINEAR);
        KeyFrame kf = new KeyFrame(Duration.millis(playtime), kv);
        tl.getKeyFrames().add(kf);
        tl.setOnFinished(e -> {
            indexCurTimeline = timelines.indexOf(tl);
            System.out.printf("The end of the message line is at %.2f%n",
                    text.getLayoutBounds().getMaxX());
        });
        timelines.add(tl);
    }
	
    /**
     * start (resumes) running all (remianing) timeline messages
     */
	private void runNewsStream() {
        timelines.forEach(Animation::play);
    }

    /**
     * clean all timelines
     */
    private void cleanTimelines() {
	    group.getChildren().remove(2, group.getChildren().size());
        timelines.clear();
	}

    /**
     * reset timelines from index to end.
     * @param index start the timelines with index to end
     */
	private void resetTimelines(int index) {
	    cleanTimelines();
        try {
            feeder.fillNewsBuffer(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        setUpTimeline(index);
        runNewsStream();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("News just in...");
		feeder = new Feeder();
		group = new Group();
        setUpMenu(primaryStage, group);
        setUpCheckboxs(group);

        Scene scene = new Scene(group, displayWidth, displayHeight);
        scene.getStylesheets().add("view/styles.css");

        // initialise all timelines for text messages and run them
		primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> Platform.exit());
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }

    /**
     * set up menu
     * menuFile for file operation, include open, close, exit
     * menuControl for controling the timelines, include start, pause, resume, reset, faster, slower, previous, next
     * @param primaryStage for open a file, need the stage
     * @param group add to group
     *
     */
    public void setUpMenu(Stage primaryStage, Group group) {
        MenuBar menuBar = new MenuBar();

        Menu menuFile = new Menu("File");
        MenuItem open = new MenuItem("Open");
        MenuItem close = new MenuItem("Close");
        close.setDisable(true);
        MenuItem quit = new MenuItem("Quit");
        Menu menuControl = new Menu("Control");
        MenuItem start = new MenuItem("Start");
        start.setDisable(true);
        MenuItem pause = new MenuItem("Pause");
        pause.setDisable(true);
        MenuItem resume = new MenuItem("Resume");
        resume.setDisable(true);
        MenuItem reset = new MenuItem("Reset");
        reset.setDisable(true);
        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
        MenuItem faster = new MenuItem("Faster");
        faster.setDisable(true);
        MenuItem slower = new MenuItem("Slower");
        slower.setDisable(true);
        MenuItem next = new MenuItem("Next");
        next.setDisable(true);
        MenuItem previous = new MenuItem("Previous");
        previous.setDisable(true);

        menuFile.getItems().addAll(open, close, quit);
        menuControl.getItems().addAll(start, reset, pause, resume, separatorMenuItem, faster, slower, next, previous);
        menuBar.getMenus().addAll(menuFile, menuControl);

        open.setOnAction((ActionEvent t) -> {
            cleanTimelines();
            FileChooser fileChooser = new FileChooser();
            file = fileChooser.showOpenDialog(primaryStage);
            start.setDisable(false);
            close.setDisable(false);
            hbox.setDisable(false);
        });

        close.setOnAction((ActionEvent t) -> {
            cleanTimelines();
            file = null;
            close.setDisable(true);
            hbox.setDisable(true);
        });

        quit.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
        quit.setOnAction((ActionEvent t) -> {
            System.exit(0);
        });

        start.setOnAction((ActionEvent t) -> {
            try {
                setUpFeed(file.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            indexCurTimeline = 0;
            setUpTimeline(indexCurTimeline);
            runNewsStream();

            menuControl.getItems().forEach(m -> m.setDisable(false));
            start.setDisable(true);
            resume.setDisable(true);
            hbox.setDisable(true);
        });

        pause.setAccelerator(KeyCombination.keyCombination("p"));
        pause.setOnAction((ActionEvent t) -> {
            timelines.forEach(tl -> tl.pause());
            pause.setDisable(!pause.isDisable());
            resume.setDisable(!resume.isDisable());
            next.setDisable(true);
            previous.setDisable(true);
        });

        resume.setAccelerator(KeyCombination.keyCombination("r"));
        resume.setOnAction((ActionEvent t) -> {
            timelines.forEach(tl -> tl.play());
            pause.setDisable(!pause.isDisable());
            resume.setDisable(!resume.isDisable());
            next.setDisable(false);
            previous.setDisable(false);
        });

        reset.setOnAction((ActionEvent t) -> {
            cleanTimelines();
            menuControl.getItems().forEach(m -> m.setDisable(true));
            start.setDisable(false);
            hbox.setDisable(false);
        });

        faster.setOnAction((ActionEvent t) -> {
            timelines.forEach(tl -> tl.setRate(tl.getRate() + 0.2));
        });

        slower.setOnAction((ActionEvent t) -> {
            timelines.forEach(tl -> tl.setRate(max(tl.getRate() - 0.2, 0)));
        });

        next.setOnAction((ActionEvent t) -> {
            if (indexCurTimeline < timelines.size()) {
                indexCurTimeline += 1;
            }
            resetTimelines(indexCurTimeline);
        });

        previous.setOnAction((ActionEvent t) -> {
            if (indexCurTimeline > 0) {
                indexCurTimeline -= 1;
            }
            resetTimelines(indexCurTimeline);
        });

        group.getChildren().addAll(menuBar);
    }

    /**
     * set up checkboxs
     * menuControl for controling the timelines, include start, pause, resume, reset, faster, slower, previous, next
     * @param group add to group
     *
     */
    public void setUpCheckboxs(Group group) {

        hbox.getStyleClass().add("hbox");
        CheckBox checkBoxAll = new CheckBox("All");

        topics.forEach(topic -> {
            CheckBox checkBox = new CheckBox(topic);
            checkBox.setSelected(true);
            checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                public void changed(ObservableValue<? extends Boolean> ov,
                                    Boolean old_val, Boolean new_val) {
                }
            });
            checkBoxs.add(checkBox);
        });

        checkBoxAll.setSelected(true);
        checkBoxAll.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                                Boolean old_val, Boolean new_val) {
                checkBoxs.forEach(c -> c.setSelected(new_val));
            }
        });
        hbox.getChildren().addAll(checkBoxs);
        hbox.getChildren().add(checkBoxAll);
        hbox.setDisable(true);
        group.getChildren().addAll(hbox);
    }
}
