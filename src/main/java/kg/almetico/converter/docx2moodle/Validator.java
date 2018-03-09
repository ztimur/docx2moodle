package kg.almetico.converter.docx2moodle;

import kg.almetico.converter.docx2moodle.model.moodle.Question;

public class Validator {

    public static void validate(Question question) throws QuestionValidationException {
        Double sum = question.getFractionsSum();
        if (sum == 0.0) {
            throw new QuestionValidationException("Correct answer not marked.", question);
        }

        if (question.getAnswers().size() < 2) {
            throw new QuestionValidationException("Answers count should be more then 1.", question);
        }

    }
}
