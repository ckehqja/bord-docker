package project.demo.domain.bord;

public enum BordType {
    HAPPY("행복한 일"), SAD("슾픈 일"), MAP("화난는 일"), ETC("기타");

    private final String description;

    BordType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
