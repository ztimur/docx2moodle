package kg.almetico.converter.docx2moodle;

import kg.almetico.converter.docx2moodle.model.moodle.Question;
import kg.almetico.converter.docx2moodle.model.moodle.Quiz;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;

public class Utils {

    public static void marshallQuiz(Quiz quiz, OutputStream outputStream) throws IOException, JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Quiz.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        jaxbMarshaller.marshal(quiz, outputStream);
    }

    public static String marshallQuestion(Question question) throws IOException, JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Question.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        StringWriter sw = new StringWriter();
        jaxbMarshaller.marshal(question, sw);

        return sw.toString();
    }
}
