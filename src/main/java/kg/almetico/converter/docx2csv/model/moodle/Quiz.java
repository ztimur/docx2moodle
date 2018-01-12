package kg.almetico.converter.docx2csv.model.moodle;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Quiz {
    @XmlElement( name = "question" )
    private List<Question> questions = new ArrayList<>();

    public void addQuestion(Question question) {
        this.questions.add(question);
    }

    public List<Question> getQuestions() {
        return questions;
    }
}
