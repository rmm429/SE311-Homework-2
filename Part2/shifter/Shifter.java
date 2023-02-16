import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Shifter extends Filter {

    private ArrayList<String> lines;

    public static void main(String args[]) {
        Shifter shifter = new Shifter();
        shifter.run();
    }

    @Override
    public void run() {

        lines = new ArrayList<>();
        OptionReader.readOptions();

        storeLines();
        shiftAllLines();
        outputLines();
    }

    private void shiftAllLines() {

        int lineCount = lines.size();
        for(int i = 0; i < lineCount; i++) {


            String origLine = lines.get(i);
            ArrayList<String> words = new ArrayList<>( // each word of the original line in a String list
                    Arrays.asList(origLine.split(" "))); // words determined by space separation
            int wordCount = words.size();

            for (int j = 1; j < wordCount; j++) {

                String lastWord = words.get(wordCount - 1);
                words.add(0, lastWord);
                words.remove(wordCount);
                lines.add(String.join(" ", words));

            }

        }
    }

    private void storeLines() {
        String buffer = read();
        if(OptionReader.getString("RemovePunct").equalsIgnoreCase("TRUE")) {
            buffer = buffer.replaceAll("[,.?;!]", ""); // remove punctuation
        }
        lines.addAll(Arrays.asList(buffer.split("\n")));
        lines.remove(0); // remove blank entry at front of list (because of rogue \n)
    }

    private void outputLines() {
        for(String line:lines) {
            write(line + "\n");
        }
    }

}
