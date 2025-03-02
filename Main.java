package pack305;

import static spark.Spark.*;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        // تشغيل خادم الويب في Thread منفصل حتى لا يمنع JavaFX من العمل
        new Thread(Main::startWebServer).start();

        // تشغيل JavaFX GUI
        launch(args);
    }

    // تشغيل خادم ويب باستخدام SparkJava
    public static void startWebServer() {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
        port(port);

        get("/", (req, res) -> "🚀 Application is running on Railway!");
        get("/hello", (req, res) -> "Hello from SparkJava!");

        // إبقاء السيرفر نشطًا
        awaitInitialization();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Media video = new Media(getClass().getResource("/image/logo.mp4").toURI().toString());
        MediaPlayer player = new MediaPlayer(video);
        MediaView view = new MediaView(player);

        player.setAutoPlay(true);

        StackPane root = new StackPane(view);
        Scene scene = new Scene(root, 900, 700);

        stage.setTitle("Logo");
        stage.setScene(scene);
        stage.show();
    }
}
