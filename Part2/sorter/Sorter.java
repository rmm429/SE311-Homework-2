import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Sorter extends Filter {

    private ArrayList<String> lines;

    public static void main(String args[]) {
        Sorter sorter = new Sorter();
        sorter.run();
    }

    @Override
    public void run() {

        lines = new ArrayList<>();
        OptionReader.readOptions();

        storeLines();
        sortLines();
        outputLines();

    }

    private void sortLines() {

        int lineCount = lines.size();

        for (int i = 0; i < lineCount; i++) {

            for (int j = i + 1; j < lineCount; j++) {
                String curLine = lines.get(i);
                String nextLine =  lines.get(j);

                if(curLine.compareToIgnoreCase(nextLine) > 0) { // Uppercase and lowercase letters will be treated the same
                    Collections.swap(lines, i, j);
                }
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
