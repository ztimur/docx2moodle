package kg.almetico.converter.docx2moodle.model.moodle;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

public class Answer {
    @XmlAttribute(name = "fraction")
    private Double fraction = 0.0;

    @XmlAttribute(name = "format")
    private String format = "html";

    @XmlElement(name = "text")
    private String text="";

    @XmlElement(name = "feedback")
    private Feedback feedback = new Feedback("");

    public Answer(Double fraction, String text) {
        this.fraction = fraction;
        this.text = "<![CDATA["+text+"]]>";
    }

    @XmlTransient
    public Double getFraction() {
        return fraction;
    }

    public void setFraction(Double fraction) {
        this.fraction = fraction;
    }
}
