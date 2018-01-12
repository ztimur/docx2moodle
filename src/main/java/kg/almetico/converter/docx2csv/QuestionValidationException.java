package kg.almetico.converter.docx2csv;

import kg.almetico.converter.docx2csv.model.moodle.Question;

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
