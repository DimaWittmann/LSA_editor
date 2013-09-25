package parser;

/**
 * Помилка, що виникає при лексичному аналізу ЛСА
 * @author Wittmann
 */
public class ParseException extends Exception {

    public ParseException() {
    }

    /**
     * @param msg the detail message.
     */
    public ParseException(String msg) {
        super(msg);
    }
}
