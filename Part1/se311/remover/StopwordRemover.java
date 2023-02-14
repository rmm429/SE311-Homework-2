package se311.remover;

import se311.helpers.Filter;

import java.io.*;
import java.util.*;

public class StopwordRemover extends Filter {

    private ArrayList<String> lines;
    private ArrayList<String> stopwords;

    @Override
    public void run() {

        lines = new ArrayList<>();
        stopwords = new ArrayList<>();
        OptionReader.readOptions();

        determineInputType();
        getStopwords();
        removeStopwords();
        determineOutputType();

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

    private void determineInputType() {

        if(in == null) { // no in pipe

            String inputType = OptionReader.getString("InputType").toUpperCase();
            if(inputType.equals("CONSOLE")) {
                acceptConsoleInput();
            } else if (inputType.equals("FILE")) {
                acceptFileInput();
            } else {
                System.err.println("ERROR: Output type invalid [config.properties]");
            }

        } else { // in pipe exists
            acceptPipeInput();
        }

    }

    private void acceptPipeInput() {

        try {
            while(true) {
                lines.add(read());
            }
        } catch (EOFException e) {}

        in.close();

    }

    private void acceptConsoleInput() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your sentences: (enter a blank line to stop)");
        String curLine = scanner.nextLine();

        while(true) { // will break when a blank line is entered

            if (curLine.isEmpty()) { // blank line stops entry
                break;
            }

            if(OptionReader.getString("RemovePunct").equalsIgnoreCase("TRUE")) {
                curLine = curLine.replaceAll("[,.?;!]", ""); // remove punctuation
            }

            lines.add(curLine);
            curLine = scanner.nextLine();

        }

    }

    private void acceptFileInput() {

        String inputFile = OptionReader.getString("Root") // input file root from config file
                + OptionReader.getString("InputFileName"); // input file name from config file

        try {

            Scanner scanner = new Scanner(new File(inputFile));

            while (scanner.hasNextLine()) {

                String curLine = scanner.nextLine();
                if(OptionReader.getString("RemovePunct").equalsIgnoreCase("TRUE")) {
                    curLine = curLine.replaceAll("[,.?;!]", ""); // remove punctuation
                }
                lines.add(curLine); // remove punctuation

            }

            scanner.close();

        } catch (FileNotFoundException e) {
            System.err.println("ERROR: Input file not found [config.properties]");
        }

    }

    private void determineOutputType() {

        if(out == null) { // no out pipe

            String outputType = OptionReader.getString("OutputType").toUpperCase();
            if(outputType.equals("CONSOLE")) {
                outputToConsole();
            } else if (outputType.equals("FILE")) {
                outputToFile();
            } else {
                System.err.println("ERROR: Output type invalid [config.properties]");
            }

        } else { // out pipe exists
            outputToPipe();
        }

    }

    private void outputToPipe() {

        for(String line:lines) {
            write(line);
        }

        out.close();

    }

    private void outputToConsole() {

        for(String line:lines) {
            System.out.println(line);
        }

    }

    private void outputToFile() {

        String outputFile = OptionReader.getString("Root") // output file root from config file
                + OptionReader.getString("OutputFileName"); // output file name from config file

        try {

            FileWriter fileWriter = new FileWriter(outputFile, false);

            for(int i = 0; i < lines.size(); i++) {
                fileWriter.write(lines.get(i));
                if(i != lines.size() - 1) { // separates each line with a newline except for the last line
                    fileWriter.write("\n");
                }
            }

            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
