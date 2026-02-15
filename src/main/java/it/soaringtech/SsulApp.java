package it.soaringtech;

import com.gluonhq.charm.glisten.visual.Swatch;
import it.soaringtech.util.ViewSwitcher;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SsulApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // 1. 메인 FXML 로드
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/soaringtech/main.fxml")); // 경로 주의!
        Parent root = loader.load();

        // 2. Scene 생성
        Scene scene = new Scene(root);
        Swatch.BLUE.assignTo(scene);

        // 3. 화면 전환기(ViewSwitcher)에 이 Scene을 등록
        ViewSwitcher.setScene(scene);

        stage.setTitle("썰 모음집");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}