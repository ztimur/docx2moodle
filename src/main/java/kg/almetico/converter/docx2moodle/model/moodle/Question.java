package kg.almetico.converter.docx2moodle.model.moodle;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Question {
    @XmlAttribute
    private String type = "multichoice";

    @XmlElement(name = "name")
    private QuestionName name;

    @XmlElement(name = "questiontext")
    private QuestionText text;

    @XmlElement(name = "generalfeedback")
    private Feedback generalFeedback = new Feedback("");

    @XmlElement(name = "defaultgrade")
    private Double defaultgrade = 5.0;

    @XmlElement(name = "penalty")
    private Double penalty = 0.3333333;

    @XmlElement(name = "hidden")
    private Integer hidden = 0;

    @XmlElement(name = "single")
    private Boolean single = Boolean.TRUE;

    @XmlElement(name = "shuffleanswers")
    private Boolean shuffleanswers = Boolean.TRUE;

    @XmlElement(name = "answernumbering")
    private String answernumbering = "none";

    @XmlElement(name = "correctfeedback")
    private Feedback correctfeedback = new Feedback("Ваш ответ верный.");

    @XmlElement(name = "partiallycorrectfeedback")
    private Feedback partiallycorrectfeedback = new Feedback("Ваш ответ частично правильный.");

    @XmlElement(name = "incorrectfeedback")
    private Feedback incorrectfeedback = new Feedback("Ваш ответ неправильный.");

    @XmlElement(name = "shownumcorrect")
    private String shownumcorrect = "";

    @XmlElement(name = "answer")
    private List<Answer> answers = new ArrayList<>();

    public Question() {
    }

    public Question(QuestionName name) {
        this.name = name;
        this.text = new QuestionText("");
    }

    public void addPrompt(String text) {
        this.text.add(text);
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public QuestionName getName() {
        return name;
    }

    public void setName(QuestionName name) {
        this.name = name;
    }

    public QuestionText getText() {
        return text;
    }

    public void setText(QuestionText text) {
        this.text = text;
    }

    public Feedback getGeneralFeedback() {
        return generalFeedback;
    }

    public void setGeneralFeedback(Feedback generalFeedback) {
        this.generalFeedback = generalFeedback;
    }

    public Double getDefaultgrade() {
        return defaultgrade;
    }

    public void setDefaultgrade(Double defaultgrade) {
        this.defaultgrade = defaultgrade;
    }

    public Double getPenalty() {
        return penalty;
    }

    public void setPenalty(Double penalty) {
        this.penalty = penalty;
    }

    public Integer getHidden() {
        return hidden;
    }

    public void setHidden(Integer hidden) {
        this.hidden = hidden;
    }

    public Boolean getSingle() {
        return single;
    }

    public void setSingle(Boolean single) {
        this.single = single;
    }

    public Boolean getShuffleanswers() {
        return shuffleanswers;
    }

    public void setShuffleanswers(Boolean shuffleanswers) {
        this.shuffleanswers = shuffleanswers;
    }

    public String getAnswernumbering() {
        return answernumbering;
    }

    public void setAnswernumbering(String answernumbering) {
        this.answernumbering = answernumbering;
    }

    public Feedback getCorrectfeedback() {
        return correctfeedback;
    }

    public void setCorrectfeedback(Feedback correctfeedback) {
        this.correctfeedback = correctfeedback;
    }

    public Feedback getPartiallycorrectfeedback() {
        return partiallycorrectfeedback;
    }

    public void setPartiallycorrectfeedback(Feedback partiallycorrectfeedback) {
        this.partiallycorrectfeedback = partiallycorrectfeedback;
    }

    public Feedback getIncorrectfeedback() {
        return incorrectfeedback;
    }

    public void setIncorrectfeedback(Feedback incorrectfeedback) {
        this.incorrectfeedback = incorrectfeedback;
    }

    public String getShownumcorrect() {
        return shownumcorrect;
    }

    public void setShownumcorrect(String shownumcorrect) {
        this.shownumcorrect = shownumcorrect;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public Double getFractionsSum() {
        Double result = 0.0;
        for (Answer answer : this.answers) {
            result += answer.getFraction();
        }
        return result;
    }

    public void adjustAnswers() {
        int counter = 0;
        for (Answer answer : this.answers) {
            if (answer.getFraction() > 0.0) {
                counter++;
            }
        }

        if (counter > 1) {
            this.single = false;
            double fractionsValue = 100.0 / counter;
            for (Answer answer : this.answers) {
                if (answer.getFraction() > 0.0) {
                    answer.setFraction(fractionsValue);
                }
            }
        }
    }

    public int getFractionsCount() {
        int counter = 0;
        for (Answer answer : this.answers) {
            if (answer.getFraction() > 0.0) {
                counter++;
            }
        }

        return counter;
    }


}
