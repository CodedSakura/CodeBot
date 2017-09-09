package parser.error;

public class ParserSymbolException extends RuntimeException {
    public ParserSymbolException(String symbol) {
        super("Unexpected symbol \"" + symbol + "\"");
    }
}
