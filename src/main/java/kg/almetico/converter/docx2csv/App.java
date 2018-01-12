package kg.almetico.converter.docx2csv;

import kg.almetico.converter.docx2csv.model.moodle.Quiz;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public class App {


    public static void main(String[] args) throws IOException, JAXBException {
        DocParser docParser = new DocParser();

        try {
            docParser.parse("assets/assessment.docx");
            Quiz quiz = docParser.getQuiz();
            Utils.marshallQuiz(quiz, System.out);
        } catch (QuestionValidationException e) {
            System.out.println(String.format("Validation error: %s. \n %s", e.getMessage(), Utils.marshallQuestion(e.getQuestion())));
        }


    }
}