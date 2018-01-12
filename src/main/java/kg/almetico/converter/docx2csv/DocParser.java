package kg.almetico.converter.docx2csv;

import kg.almetico.converter.docx2csv.model.moodle.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DocParser {
    static Pattern CHOICE_PATTERN = Pattern.compile("\\s*(\\*)?([абвгде])+\\s*\\)(.*)");
    private Quiz quiz = new Quiz();

    public DocParser() {
    }

    public void parse(String fileName) throws IOException, QuestionValidationException {
        FileInputStream is = new FileInputStream(fileName);
        try {
            XWPFDocument doc = new XWPFDocument(is);


            List<XWPFParagraph> paragraphs = doc.getParagraphs();

            Iterator<XWPFParagraph> iterator = paragraphs.iterator();

            while (iterator.hasNext()) {
                Question question = findQuestion(iterator);

                if (question != null) {
                    Validator.validate(question);
                    quiz.addQuestion(question);
                }
            }

            System.out.println(String.format("Found %s items.", quiz.getQuestions().size()));
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    private Question findQuestion(Iterator<XWPFParagraph> iterator) {
        Question result = null;
        boolean done = false;
        while (iterator.hasNext() && !done) {
            XWPFParagraph p = iterator.next();
            String text = p.getText();
            if (isQuestionStarted(text)) {
                if (text.length() < 2) {
                    throw new IllegalStateException(String.format("Question body could not be null. [%s]", text));
                }
                text = text.substring(text.indexOf('#') + 1).trim();

                result = new Question(new QuestionName(text), new QuestionText(text));
                while (iterator.hasNext() && !done) {
                    XWPFParagraph bp = iterator.next();
                    String t = bp.getText();
                    if (StringUtils.isBlank(t)) {
                        done = true;
                    } else {
                        while (iterator.hasNext() && !done) {
                            Matcher matcher = CHOICE_PATTERN.matcher(t);
                            if (matcher.matches()) {
//                                System.out.println(String.format("0->%s 1->%s 2->%s 3->%s", matcher.group(0), matcher.group(1), matcher.group(2), matcher.group(3)));
                                if (StringUtils.isBlank(matcher.group(3))) {
                                    throw new IllegalStateException(String.format("Answer body could not be null. [%s]", text));
                                }
                                Double fraction = matcher.group(1) != null ? 100.0 : 0.0;
                                Answer answer = new Answer(fraction, matcher.group(3).trim());
                                result.addAnswer(answer);
                                t = iterator.next().getText();
                            } else {
                                done = true;
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    private boolean isQuestionStarted(String text) {
        if (StringUtils.isBlank(text)) {
            return false;
        }

        return text.matches("(.*)?#.*");
    }

    public Quiz getQuiz() {
        return quiz;
    }
}
