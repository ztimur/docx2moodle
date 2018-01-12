package kg.almetico.converter.docx2csv;

import java.util.ArrayList;
import java.util.List;

public class AssessmentItem {
    private String body;
    private List<AssessmentItemChoice> choices = new ArrayList<>();

    public AssessmentItem(String body) {
        this.body = body;
    }

    public void addChoice(String body, Double weight) {
        AssessmentItemChoice e = new AssessmentItemChoice(body, weight);
        this.choices.add(e);
    }

    public String getBody() {
        return body;
    }

    public List<AssessmentItemChoice> getChoices() {
        return choices;
    }

    @Override
    public String toString() {
        return "AssessmentItem{" +
                "body='" + body + '\'' +
                ", choices=" + choices +
                '}';
    }
}
