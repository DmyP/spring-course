package util.exceptions;

public class ExportPdfException extends RuntimeException {
    public ExportPdfException(Exception e) {
        super(e);
    }
}
