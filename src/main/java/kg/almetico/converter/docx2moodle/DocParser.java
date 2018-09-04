package kg.almetico.converter.docx2moodle;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import kg.almetico.converter.docx2moodle.model.moodle.Answer;
import kg.almetico.converter.docx2moodle.model.moodle.Question;
import kg.almetico.converter.docx2moodle.model.moodle.QuestionName;
import kg.almetico.converter.docx2moodle.model.moodle.Quiz;


public class DocParser
{
    public static final String QUESTION_PATTERN = "(.*)?[\\*]{3}([0-9]{3})\\.([0-9]+)\\.([0-9]{1})\\.?(.*)";
    static Pattern CHOICE_PATTERN =
            Pattern.compile( "\\s*(\\*)?([123456789абвгдежАБВГДЕЖ1234567abcdefghABCDEFGH])+\\s*[).]\\s*(\\*)?(.*)" );
    private Quiz quiz = new Quiz();


    public DocParser()
    {
    }


    public void parse( String fileName ) throws IOException, QuestionValidationException
    {
        FileInputStream is = new FileInputStream( fileName );
        try
        {
            XWPFDocument doc = new XWPFDocument( is );


            List<XWPFParagraph> paragraphs = doc.getParagraphs();

            Iterator<XWPFParagraph> iterator = paragraphs.iterator();

            while ( iterator.hasNext() )
            {
                Question question = findQuestion( iterator );

                if ( question != null )
                {
                    Validator.validate( question );
                    question.adjustAnswers();
                    quiz.addQuestion( question );
                }
            }

            System.out.println( String.format( "Found %s items.", quiz.getQuestions().size() ) );
        }
        finally
        {
            if ( is != null )
            {
                is.close();
            }
        }
    }


    private Question findQuestion( Iterator<XWPFParagraph> iterator )
    {
        Question result = null;
        boolean done = false;
        while ( iterator.hasNext() && !done )
        {
            XWPFParagraph p = iterator.next();
            String text = p.getText();
            if ( isQuestionStarted( text ) )
            {
                if ( text.length() < 2 )
                {
                    throw new IllegalStateException( String.format( "Question body could not be null. [%s]", text ) );
                }

                Pattern pattern = Pattern.compile( QUESTION_PATTERN );

                final Matcher m = pattern.matcher( text );

                int questionId;
                int correctOption;
                int complexity;
                if ( m.matches() )
                {
                    System.out.println(
                            String.format( "%s %s %s %s", m.group( 1 ), m.group( 2 ), m.group( 3 ), m.group( 4 ) ) );
                    questionId = Integer.parseInt( m.group( 2 ) );
                    correctOption = Integer.parseInt( m.group( 3 ) );
                    complexity = Integer.parseInt( m.group( 4 ) );
                }
                else
                {
                    continue;
                }

                String text1 = StringEscapeUtils.escapeHtml4( m.group( 5 ) );
                result = new Question( questionId, new QuestionName( questionId + "." + text1 ), complexity );
                result.addPrompt( text1 + "<br/>" );
                while ( iterator.hasNext() && !done )
                {
                    XWPFParagraph bp = iterator.next();
                    String t = bp.getText();
                    if ( StringUtils.isBlank( t ) )
                    {
                        done = true;
                    }
                    else
                    {
                        int counter = 0;
                        while ( iterator.hasNext() && !done )
                        {
                            Matcher matcher = CHOICE_PATTERN.matcher( t );
                            if ( matcher.matches() )
                            {
                                counter++;
                                //                                System.out.println(String.format("0->%s 1->%s 2->%s
                                // 3->%s", matcher.group(0), matcher.group(1), matcher.group(2), matcher.group(3)));
                                if ( StringUtils.isBlank( matcher.group( 4 ) ) )
                                {
                                    throw new IllegalStateException(
                                            String.format( "Answer body could not be null. [%s]", text ) );
                                }
                                //                                Double fraction =
                                //                                        matcher.group( 1 ) != null ? 100.0 :
                                // matcher.group( 3 ) != null ? 100 : 0.0;
                                Double fraction = counter == correctOption ? 100.0 : 0.0;
                                String t1 = matcher.group( 4 ).trim();
                                Answer answer = new Answer( fraction, StringEscapeUtils.escapeHtml4( t1 ) );
                                result.addAnswer( answer );
                                t = iterator.next().getText();
                            }
                            else
                            {
                                if ( StringUtils.isBlank( t ) )
                                {
                                    done = true;
                                }
                                else
                                {
                                    System.out.println( t );
                                    result.addPrompt( StringEscapeUtils.escapeHtml4( t ) + "<br/>" );
                                    t = iterator.next().getText();
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }


    private boolean isQuestionStarted( String text )
    {
        if ( StringUtils.isBlank( text ) )
        {
            return false;
        }

        return text.matches( QUESTION_PATTERN );
    }


    public Quiz getQuiz()
    {
        return quiz;
    }
}
