package kg.almetico.converter.docx2csv;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class XWPFData {
    public static XWPFDocument openDocument(String fileName) throws IOException {
        return new XWPFDocument(new FileInputStream(fileName));
    }

    public static XWPFDocument writeOutAndReadBack(XWPFDocument doc) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
        doc.write(baos);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        return new XWPFDocument(bais);
    }


}
