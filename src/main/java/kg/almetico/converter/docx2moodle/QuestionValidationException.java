package kg.almetico.converter.docx2moodle;

import kg.almetico.converter.docx2moodle.model.moodle.Question;

public class QuestionValidationException extends Exception {
    private Question question;

    public QuestionValidationException(String message, Question question) {
        super(message);
        this.question = question;
    }

    public Question getQuestion() {
        return question;
    }
}
