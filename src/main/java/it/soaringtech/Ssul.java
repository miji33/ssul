package it.soaringtech;

public class Ssul {
    private int id;             // 번호
    private String title;       // 제목
    private String content;     // 내용
    private String category;    // 분류
    private String regDate;   // 작성일

    public Ssul(int id, String title, String content, String category, String regDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
        this.regDate = regDate;
    }

    // 데이터를 새로 만들 때 쓸 생성자
    public Ssul(String title, String content, String category, String regDate) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.regDate = regDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getRegDate() { return regDate; }
    public void setRegDate(String regDate) { this.regDate = regDate; }

    // 테스트용 출력 편의 메서드
    @Override
    public String toString() {
        return "제목: " + title + " (" + category + ")";
    }
}