package kg.almetico.converter.docx2csv;

public class AssessmentItemChoice {
    private String body;
    private Double weight;

    public AssessmentItemChoice(String body, Double weight) {
        this.body = body;
        this.weight = weight;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "AssessmentItemChoice{" +
                "body='" + body + '\'' +
                ", weight=" + weight +
                '}';
    }
}
