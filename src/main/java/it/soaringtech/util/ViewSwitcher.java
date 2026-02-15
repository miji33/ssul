package it.soaringtech.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ViewSwitcher {

    private static Scene scene;

    // 1. 메인 앱(Main.java)에서 처음에 Scene을 넘겨받을 때 사용
    public static void setScene(Scene scene) {
        ViewSwitcher.scene = scene;
    }

    // 2. 화면 전환 메서드 (파일명만 넣으면 이동!)
    // 예: ViewSwitcher.switchTo("write");
    public static void switchTo(String viewName) {
        try {
            // FXML 파일 로드 (경로는 프로젝트 구조에 따라 다를 수 있음, resources/com/miji/ssul/view/ 라고 가정)
            // 주의: FXML 파일 위치가 resources 루트라면 "/viewName.fxml"로 수정 필요
            FXMLLoader loader = new FXMLLoader(ViewSwitcher.class.getResource("/it/soaringtech/" + viewName + ".fxml"));
            Parent root = loader.load();

            // 현재 Scene의 뿌리(Root)를 새로운 화면으로 교체
            scene.setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("화면 전환 실패: " + viewName + ".fxml 파일을 찾을 수 없습니다.");
        }
    }
}
