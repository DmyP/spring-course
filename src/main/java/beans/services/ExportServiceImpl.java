package beans.services;

import beans.models.Ticket;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.exceptions.ExportPdfException;

import java.io.IOException;
import java.util.List;

@Service("exportServiceImpl")
@Transactional
public class ExportServiceImpl implements ExportService<Ticket> {

    @Override
    public String exportEntities(List<Ticket> tickets) {
        final PDFont font = PDType1Font.TIMES_BOLD;
        final int fontSize = 12;
        String path = System.getProperty("java.io.tmpdir") +
                (tickets.get(0).getEvent().getName() +  ".pdf").replace(" ", "_");
        try(PDDocument file = new PDDocument()) {
            for (Ticket ticket : tickets) {
                PDPage page = new PDPage();
                file.addPage(page);
                PDPageContentStream stream = new PDPageContentStream(file, page);
                stream.setFont(font, fontSize);
                stream.beginText();
                stream.newLineAtOffset(20, 20);
                stream.showText(ticket.getEvent().getName());
                stream.newLineAtOffset(10, 30);
                stream.showText("Auditorium: " + ticket.getEvent().getAuditorium().getName());
                stream.newLineAtOffset(10, 40);
                stream.showText("DateTime: " + ticket.getDateTime());
                stream.newLineAtOffset(10, 50);
                stream.showText("Price: " + ticket.getPrice());
                stream.newLineAtOffset(10, 60);
                stream.showText("UserName: " + ticket.getUser().getName());
                stream.newLineAtOffset(10, 70);
                stream.endText();
                stream.close();
            }
            file.save(path);
        } catch (IOException e) {
            throw new ExportPdfException(e);
        }
        return path;
    }
}
