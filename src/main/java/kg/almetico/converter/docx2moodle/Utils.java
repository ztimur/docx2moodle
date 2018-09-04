package kg.almetico.converter.docx2moodle;

import kg.almetico.converter.docx2moodle.model.moodle.Question;
import kg.almetico.converter.docx2moodle.model.moodle.Quiz;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import com.sun.xml.internal.bind.marshaller.DataWriter;


public class Utils {

    public static String marshallQuiz(Quiz quiz) throws IOException, JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Quiz.class);
        Marshaller marshaller = jaxbContext.createMarshaller();

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        // Set UTF-8 Encoding
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        // The below code will take care of avoiding the conversion of < to &lt; and > to &gt; etc
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        DataWriter dataWriter = new DataWriter(printWriter, "UTF-8", new NoEscapeHandler());

        marshaller.marshal(quiz, dataWriter);

        return stringWriter.toString();
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
