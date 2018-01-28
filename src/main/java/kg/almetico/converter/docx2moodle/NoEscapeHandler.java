package kg.almetico.converter.docx2moodle;

import com.sun.xml.internal.bind.marshaller.CharacterEscapeHandler;

import java.io.IOException;
import java.io.Writer;

public class NoEscapeHandler implements CharacterEscapeHandler {

    @Override
    public void escape(char[] buf, int start, int len, boolean isAttValue,
                       Writer out) throws IOException {
        for (int i = start; i < start + len; i++) {
            char ch = buf[i];
            out.write(ch);
        }
    }
}
