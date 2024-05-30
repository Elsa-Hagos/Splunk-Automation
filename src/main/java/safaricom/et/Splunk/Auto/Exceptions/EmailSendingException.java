package safaricom.et.Splunk.Auto.Exceptions;

public class EmailSendingException extends Exception {
    public EmailSendingException(String message, Throwable cause) {
        super(message, cause);
    }
}
