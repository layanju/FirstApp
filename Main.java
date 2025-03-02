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
        // التحقق مما إذا كان التطبيق يعمل على Railway
        boolean isRailway = System.getenv("RAILWAY") != null;

        // تشغيل خادم الويب فقط على Railway
        if (isRailway) {
            startWebServer();
        } else {
            // تشغيل خادم الويب في Thread منفصلة عند التشغيل محليًا
            new Thread(Main::startWebServer).start();

            // تشغيل JavaFX فقط على الجهاز المحلي
            launch(args);
        }
    }

    // تشغيل خادم ويب باستخدام SparkJava
    public static void startWebServer() {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
        port(port);

        get("/", (req, res) -> {
            res.type("text/html");
            return "<h1>🚀 Application is running on Railway!</h1>";
        });

        // إبقاء السيرفر نشطًا
        awaitInitialization();

        // حل المشكلة: إبقاء التطبيق يعمل إلى أجل غير مسمى
        while (true) {
            try {
                Thread.sleep(100000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        // تشغيل JavaFX فقط عند التشغيل محليًا
        System.out.println("✅ JavaFX يعمل محليًا فقط!");

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
