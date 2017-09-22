package parser.error;

public class ParserNumberException extends ParserException {
    public ParserNumberException(String number) {
        super("Invalid number \"" + number + "\"");
    }
}
