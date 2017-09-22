package parser.error;

import parser.Parser;

public class ParserException extends RuntimeException {
    public ParserException() {
        super();
    }
    public ParserException(String s) {
        super(s);
    }
}
