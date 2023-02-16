import java.util.ArrayList;
import java.util.Arrays;

public class Search extends Filter {

    private ArrayList<String> lines;

    public static void main(String args[]) {
        Search search = new Search();
        search.run();
    }

    @Override
    public void run() {

        lines = new ArrayList<>();
        OptionReader.readOptions();

        storeLines();
        searchForKeyword();
        outputLines();

    }

    private void searchForKeyword() {

        String keyword = OptionReader.getString("Keyword");

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