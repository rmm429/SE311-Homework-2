import java.io.*;
import java.util.*;

public class StopwordRemover extends Filter {

    private ArrayList<String> lines;
    private ArrayList<String> stopwords;

    public static void main(String args[]) {
        StopwordRemover remover = new StopwordRemover();
        remover.run();
    }

    @Override
    public void run() {

        lines = new ArrayList<>();
        stopwords = new ArrayList<>();
        OptionReader.readOptions();

        storeLines();
        getStopwords();
        removeStopwords();
        outputLines();

    }

    private void getStopwords() {

        String stopwordsString = OptionReader.getString("Stopwords");  // stopwords list String from config file
        stopwords = new ArrayList<>( // each word of the stopwords in an ArrayList
                Arrays.asList(stopwordsString.split(";"))); // words determined by semicolon separation

    }

    private void removeStopwords() {

        for(int i = 0; i < lines.size(); i++) {

            String curLine = lines.get(i);
            ArrayList<String> lineWords = new ArrayList<>(
                    Arrays.asList(curLine.split(" ")));

            for(String stopword:stopwords) {
                lineWords.removeIf(curWord -> curWord.equalsIgnoreCase(stopword)); // case-insensitive removal
            }

            lines.set(i, String.join(" ", lineWords)); // replace old line with updated line

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
