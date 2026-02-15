package it.soaringtech.controller;

import it.soaringtech.Ssul;
import it.soaringtech.SsulDao;
import it.soaringtech.util.ViewSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class DetailController implements Initializable {
    @FXML public TextField txtCategoryDetail;
    @FXML public TextField txtTitleDetail;
    @FXML public TextArea txtAreaContentDetail;
    @FXML public Button btnBack;
    @FXML public Button btnEdit;
    @FXML public Button btnDDelete;

    public static Ssul currentSsul;

    private SsulDao dao = new SsulDao();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (currentSsul != null) {
            System.out.println("[상세] 데이터 수신 성공: " + currentSsul.getTitle());

            txtTitleDetail.setText(currentSsul.getTitle());
            txtCategoryDetail.setText(currentSsul.getCategory());
            txtAreaContentDetail.setText(currentSsul.getContent());
        } else {
            System.out.println("[상세] 데이터가 없습니다. (테스트 실행 중인가요?)");
        }

        // 읽기 전용이니까 수정 못하게 막기
        txtTitleDetail.setEditable(false);
        txtCategoryDetail.setEditable(false);
        txtAreaContentDetail.setEditable(false);
    }

    @FXML
    public void onBackClick(ActionEvent actionEvent) {
        System.out.println("[상세] 목록으로 돌아갑니다.");
        currentSsul = null;
        ViewSwitcher.switchTo("main");
    }

    @FXML
    public void onEditClick(ActionEvent actionEvent) {
        System.out.println("[상세] 수정 버튼 클릭! 글쓰기 화면으로 이동합니다.");

        // 현재 보고 있는 썰 데이터가 정상적으로 존재하는지 안전하게(방어적으로) 확인
        if (currentSsul != null) {
            // 글쓰기 화면으로 전환!
            ViewSwitcher.switchTo("write");
        } else {
            // 혹시라도 데이터가 꼬여서 비어있는 경우를 대비한 예외 처리
            System.out.println("⚠️ 오류: 현재 선택된 썰 데이터가 없습니다.");
        }
    }

    @FXML
    public void onDeleteClick(ActionEvent actionEvent) {
        if (currentSsul == null) return;

        // 진짜 삭제할지 물어보는 팝업창 (Alert)
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("삭제 확인");
        alert.setHeaderText(null);
        alert.setContentText("정말 이 썰을 삭제하시겠습니까?");

        // 사용자가 OK를 눌렀는지 확인
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

            // 1. DB에서 삭제
            dao.delete(currentSsul.getId());
            System.out.println("[상세] 삭제 완료! ID: " + currentSsul.getId());

            // 2. 메인 화면으로 복귀
            currentSsul = null; // 데이터 비우기
            ViewSwitcher.switchTo("main");
        }
    }
}
