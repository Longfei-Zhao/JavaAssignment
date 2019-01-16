import javafx.application.*;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.stage.*;

public class ImageDemo extends Application {
   public void start(Stage stage) {
      Image image = new Image("amazing-trees.jpg");
      // the stream processing, toImage is the terminal method
      // which breaks laziness
      Image finalImage = LatentImage
         .from(image)
         .transform(Color::brighter)
         .transform(Color::grayscale)
         .toImage();      
      
	  stage.setScene(new Scene(new VBox(
         new ImageView(image),
         new ImageView(finalImage))));
      stage.show();
   }
}
