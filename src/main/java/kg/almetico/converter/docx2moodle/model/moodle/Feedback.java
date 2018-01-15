package kg.almetico.converter.docx2moodle.model.moodle;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Feedback {
    @XmlAttribute(name = "format")
    private String format = "html";

    @XmlElement(name = "text")
    private String text;


    public Feedback(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
