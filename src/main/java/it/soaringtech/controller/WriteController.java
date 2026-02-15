package it.soaringtech.controller;

import it.soaringtech.Ssul;
import it.soaringtech.SsulDao;
import it.soaringtech.util.ViewSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class WriteController implements Initializable {
    // FXML 연결 확인
    @FXML public Button btnSave;
    @FXML public Button btnCancel;
    @FXML private TextField txtTitle;
    @FXML private TextArea txtAreaContent;
    @FXML private ComboBox<String> comboCategory;

    // DAO 생성
    private SsulDao dao = new SsulDao();

    private boolean isEditMode = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 콤보박스 초기화
        if (comboCategory != null) {
            comboCategory.getItems().addAll("재미", "감동", "공포", "기타");
            comboCategory.getSelectionModel().selectFirst(); // 기본값 선택
        }

        // 화면이 켜질 때 수정 모드인지 확인하고 데이터 채우기
        // DetailController에서 넘어온 데이터가 존재하는지 확인
        if (DetailController.currentSsul != null) {
            isEditMode = true; // 스위치를 '수정 모드'로 켬
            Ssul targetSsul = DetailController.currentSsul;

            // 기존 썰의 데이터를 화면의 텍스트필드와 콤보박스에 미리 세팅
            txtTitle.setText(targetSsul.getTitle());
            txtAreaContent.setText(targetSsul.getContent());
            comboCategory.setValue(targetSsul.getCategory());

            System.out.println("=== 수정 모드 진입: 기존 데이터를 불러왔습니다. ===");
        } else {
            isEditMode = false; // 데이터가 없으면 '새 글 쓰기 모드'
        }
    }

    @FXML
    public void onSaveClick(ActionEvent actionEvent) {
        System.out.println("=== 썰 저장 버튼 클릭됨 ===");

        // 1. 입력값 가져오기
        String title = txtTitle.getText();
        String content = txtAreaContent.getText();
        String category = comboCategory.getValue();
        String date = LocalDate.now().toString(); // 날짜 생성

        // 2. 유효성 검사 (제목 없으면 저장 안 함)
        if (title == null || title.trim().isEmpty()) {
            System.out.println("⚠️ 제목이 비어있어서 저장하지 않습니다.");
            return;
        }

        System.out.println("입력 확인 -> 제목: " + title + ", 카테고리: " + category);

        // ★ 3. 저장 버튼 클릭 시: 새 글 쓰기와 수정 모드 분기 처리
        if (isEditMode) {
            // [수정 모드일 경우]
            Ssul targetSsul = DetailController.currentSsul;

            // 화면에서 새로 입력한 값으로 객체의 데이터를 덮어씌움
            targetSsul.setTitle(title);
            targetSsul.setContent(content);
            targetSsul.setCategory(category);
            targetSsul.setRegDate(date); // 수정일자로 업데이트 하려면 추가

            // DAO의 update 메서드 호출
            dao.update(targetSsul);
            System.out.println("DB 수정 요청 완료!");

        } else {
            // [새 글 쓰기 모드일 경우] - 기존 로직과 동일
            Ssul newSsul = new Ssul(title, content, category, date);
            dao.insert(newSsul);
            System.out.println("DB 새 글 저장 요청 완료!");
        }

        // 4. 작업 완료 후 초기화
        DetailController.currentSsul = null;
        isEditMode = false;

        // 5. 메인으로 복귀
        ViewSwitcher.switchTo("main");
    }

    @FXML
    public void onCancelClick(ActionEvent actionEvent) {
        System.out.println("[취소] 작성을 취소하고 메인으로 돌아갑니다.");

        // ★ 취소하고 나갈 때도 혹시 모르니 초기화
        DetailController.currentSsul = null;
        isEditMode = false;

        ViewSwitcher.switchTo("main");
    }
}
