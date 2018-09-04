package kg.almetico.converter.docx2moodle.model.moodle;


import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
public class Quiz
{
    @XmlElement( name = "question" )
    private List<Question> questions = new ArrayList<>();


    public void addQuestion( Question question )
    {
        this.questions.add( question );
    }


    public List<Question> getQuestions()
    {
        return questions;
    }


    public void addAll( final List<Question> questions )
    {
        questions.forEach( this::addQuestion );
    }
}
