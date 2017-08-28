package codebot;

import java.io.*;

public class FileIO {
    public static String read(String filename) {
        StringBuilder out = new StringBuilder();
        BufferedReader br = null;
        try {
            String line;
            br = new BufferedReader(new FileReader("./" + filename));
            while ((line = br.readLine()) != null) out.append(line).append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return out.toString();
    }

    public static void write(String filename, String text, boolean append) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter("./" + filename, append));
            bw.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) bw.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static String[][] CSVParse(String text) {
        String[] lines = text.split("\n");
        String[][] out = new String[lines.length][0];
        for (int i = 0; i < lines.length; i++) {
            String[] values = lines[i].split(",");
            out[i] = values;
        }
        return out;
    }
}
