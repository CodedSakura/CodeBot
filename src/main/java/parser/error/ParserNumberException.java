package parser.error;

public class ParserNumberException extends RuntimeException {
    public ParserNumberException(String number) {
        super("Invalid number \"" + number + "\"");
    }
}
