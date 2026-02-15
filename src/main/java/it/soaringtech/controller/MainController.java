package it.soaringtech.controller;

import it.soaringtech.Ssul;
import it.soaringtech.SsulDao;
import it.soaringtech.util.ViewSwitcher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML public ToggleGroup categoryGroup;
    @FXML public ToggleButton tbtnAll;
    @FXML public ToggleButton tbtnFun;
    @FXML public ToggleButton tbtnTouched;
    @FXML public ToggleButton tbtnFear;
    @FXML public ListView<Ssul> lvSsulList;
    @FXML public Button fabWrite;
    private ToggleButton btnFilter;
    private ObservableList<Ssul> ssulList = FXCollections.observableArrayList();
    private SsulDao dao = new SsulDao();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. [데이터 연결] 리스트뷰와 ObservableList 연결
        lvSsulList.setItems(ssulList);

        // 2. [디자인 설정] CellFactory (글자색, 모양 꾸미기)
        lvSsulList.setCellFactory(lv -> new ListCell<Ssul>() {
            @Override
            protected void updateItem(Ssul item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle(""); // 스타일 초기화
                } else {
                    // 화면 표시 형식: [카테고리] 제목 (날짜)
                    setText(String.format("[%s] %s (%s)", item.getCategory(), item.getTitle(), item.getRegDate()));

                    // 카테고리별 글자색 변경
                    if ("재미".equals(item.getCategory())) {
                        setStyle("-fx-text-fill: #008CBA; -fx-font-weight: bold;");
                    } else if ("감동".equals(item.getCategory())) {
                        setStyle("-fx-text-fill: #D68910;");
                    } else if ("공포".equals(item.getCategory())) {
                        setStyle("-fx-text-fill: #E74C3C;");
                    } else {
                        setStyle("-fx-text-fill: black;"); // 기본 검정
                    }
                }
            }
        });

        // 3. [데이터 로드] DB에서 데이터 가져오기
        refreshList();

        // 4. [기존 코드 유지] 필터 버튼 초기화
        if (categoryGroup != null) {
            categoryGroup.selectToggle(tbtnAll);
        }

        // 5. [클릭 이벤트] 리스트 아이템 클릭 시 상세 화면 이동
        lvSsulList.setOnMouseClicked(event -> {
            // 더블 클릭(2번) 했을 때만 상세 화면으로 이동
            if (event.getClickCount() == 2) {
                Ssul selected = lvSsulList.getSelectionModel().getSelectedItem();

                if (selected != null) {
                    // 1) 데이터를 DetailController의 우체통(static 변수)에 넣음
                    DetailController.currentSsul = selected;

                    // 2) 화면 전환 (ViewSwitcher 사용)
                    ViewSwitcher.switchTo("detail");
                }
            }
        });
    }

    // [새로 추가] DB에서 데이터를 다시 불러오는 메서드 (따로 빼두면 편해요)
    public void refreshList() {
        ssulList.clear(); // 기존 목록 비우기
        ssulList.addAll(dao.selectList()); // DB에서 가져와서 채우기
        System.out.println("[메인] 리스트 갱신 완료: " + ssulList.size() + "개");
    }

    @FXML
    public void onFilterClick(ActionEvent actionEvent) {
        ToggleButton selectedBtn = (ToggleButton) categoryGroup.getSelectedToggle();

        if (selectedBtn == null) {
            System.out.println("[필터] 선택된 카테고리가 없습니다.");
            return;
        }

        ssulList.clear();
        List<Ssul> newList;

        if (selectedBtn == tbtnAll) {
            System.out.println("[필터] '전체' 보기 모드 -> 모든 썰을 불러옵니다.");
            newList = dao.selectList(); // 전체 조회
        }
        else if (selectedBtn == tbtnFun) {
            System.out.println("[필터] '재미' 카테고리 선택");
            newList = dao.selectByCategory("재미");
        }
        else if (selectedBtn == tbtnTouched) {
            System.out.println("[필터] '감동' 카테고리 선택");
            newList = dao.selectByCategory("감동");
        }
        else if (selectedBtn == tbtnFear) {
            System.out.println("[필터] '공포(진상)' 카테고리 선택");
            newList = dao.selectByCategory("공포");
        } else {
            newList = dao.selectList();
        }

        ssulList.addAll(newList);
    }

    @FXML
    public void onWriteClick(ActionEvent actionEvent) {
        System.out.println("[이벤트] 글쓰기 버튼 클릭! -> 글쓰기 화면으로 이동");
        ViewSwitcher.switchTo("write"); // write.fxml로 이동
    }
}
