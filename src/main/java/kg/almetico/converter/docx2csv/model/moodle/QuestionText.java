package kg.almetico.converter.docx2csv.model.moodle;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class QuestionText {
    @XmlAttribute(name = "format")
    private String format = "html";

    @XmlElement(name = "text")
    private String text;

    public QuestionText(String text) {
        this.text = text;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
