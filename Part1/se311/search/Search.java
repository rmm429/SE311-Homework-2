package se311.search;

import se311.helpers.Filter;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Search extends Filter {

    private ArrayList<String> lines;
    private String keyword;
    private boolean keywordNotFound;

    @Override
    public void run() {

        lines = new ArrayList<>();
        OptionReader.readOptions();

        determineInputType();
        searchForKeyword();
        determineOutputType();

    }

    private void searchForKeyword() {

        if(keyword == null) { // Search received input from pipe
            keyword = OptionReader.getString("Keyword");
        }
        keywordNotFound = false;

        // These variables outside of the for-loop ensure that  the line removal done below
        // will not interfere with incrementing through the entire list as a whole
        int curLineIndex = 0;
        int linesListSize = lines.size();

        for(int i = 0; i < linesListSize; i++) { // will execute correct number of times regardless of line removal

            String curLine = lines.get(curLineIndex);
            ArrayList<String> lineWords = new ArrayList<>(
                    Arrays.asList(curLine.split(" ")));

            // If a keyword is not found, that line will be removed from the list of lines
            if (!lineWords.contains(keyword)) {
                lines.remove(curLineIndex); // when removal is done, the line at the next index in the list becomes the current index
            } else {

                // The below will bracket the keyword in the line while also ensuring that only the
                // keyword itself is bracketed and not other words that contain the keyword within them
                // e.g. the keyword [is] will not bracket the 'is' in the word "this"
                int keywordIndex = lineWords.indexOf(keyword);
                String keywordBracketed = "[" + lineWords.get(keywordIndex) + "]";
                lineWords.set(keywordIndex, keywordBracketed);
                lines.set(curLineIndex, String.join(" ", lineWords));

                curLineIndex++; // can move on to the next index since a line with the keyword was found

            }

        }

        if(lines.isEmpty()) { // keyword not found
            lines.add("[" + keyword + "] not found");
            keywordNotFound = true;
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

    /**
     * This helper method that differs from the rest of the concrete Filters
     * This is because when getting input from console or file, the console will prompt for a keyword
     */
    private void acceptConsoleInput() {

        // Get keyword form console
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the keyword: ");
        keyword = scanner.nextLine();

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

    /**
     * This helper method that differs from the rest of the concrete Filters
     * This is because when getting input from console or file, the console will prompt for a keyword
     */
    private void acceptFileInput() {

        String inputFile = OptionReader.getString("Root") // input file root from config file
                + OptionReader.getString("InputFileName"); // input file name from config file

        // Get keyword form console
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the keyword: ");
        keyword = scanner.nextLine();

        try {

            scanner = new Scanner(new File(inputFile));

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

    /**
     * This helper method that differs from the rest of the concrete Filters
     * This is because keyword highlighting will occur when outputting to the console
     */
    private void outputToConsole() {

        // ANSI String colorization codes
        final String ANSI_RESET = "\u001B[0m";
        final String ANSI_RED = "\u001B[31m";

        if(keywordNotFound) { // keyword not found
            System.out.println("[" + ANSI_RED + keyword + ANSI_RESET + "] not found");
        } else {
            for(String line:lines) {

                // The below ensures that only the keyword itself is highlighted
                // and not other words that contain the keyword within them
                // e.g. the keyword [is] will not highlight the 'is' in the word "this"
                ArrayList<String> lineWords = new ArrayList<>(
                        Arrays.asList(line.split(" ")));
                int keywordIndex = lineWords.indexOf("[" + keyword + "]");
                String keywordRemoveBrackets = lineWords.get(keywordIndex)
                        .replaceAll("\\[|\\]", ""); // remove brackets around keyword for console output
                String keywordHighlighted = ANSI_RED + keywordRemoveBrackets + ANSI_RESET;
                lineWords.set(keywordIndex, keywordHighlighted);
                System.out.println(String.join(" ", lineWords));

            }
        }

    }

    private void outputToFile() {

        String outputFile = OptionReader.getString("Root") // output file root from config file
                + OptionReader.getString("OutputFileName"); // output file name from config file

        try {

            FileWriter fileWriter = new FileWriter(outputFile, false);

            if(lines.isEmpty()) { // keyword not found
                fileWriter.write("[" + keyword + "] not found");
            } else {

                for(int i = 0; i < lines.size(); i++) {
                    fileWriter.write(lines.get(i));
                    if(i != lines.size() - 1) { // separates each line with a newline except for the last line
                        fileWriter.write("\n");
                    }
                }

            }

            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}