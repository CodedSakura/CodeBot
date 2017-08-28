package codebot;

import org.junit.Test;

import static org.junit.Assert.*;

public class FileIOTest {
    @Test
    public void CSVParse() throws Exception {
        String[][] expected = {
            {
                "this",
                "is",
                "a",
                "test"
            }, {
                "1",
                "2",
                "3",
                "4",
                "5"
            }
        };

        String[][] actual = FileIO.CSVParse("this,is,a,test\n1,2,3,4,5");

        assertArrayEquals(expected[0], actual[0]);
        assertArrayEquals(expected[1], actual[1]);
    }

}