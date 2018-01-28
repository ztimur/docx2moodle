package kg.almetico.converter.docx2moodle;

import kg.almetico.converter.docx2moodle.model.moodle.Question;

public class Validator {

    public static void validate(Question question) throws QuestionValidationException {
        Double sum = question.getFractionsSum();

        if (Math.abs(100 - sum) > 0.1) {
            if (sum == 0.0) {
                throw new QuestionValidationException("Correct answer not marked.", question);
            } else {
                throw new QuestionValidationException(String.format("Fraction sum incorrect: %f.", sum), question);
            }
        }
    }
}
