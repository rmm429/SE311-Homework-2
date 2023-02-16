import java.util.ArrayList;
import java.util.Arrays;

public class Input extends Filter {

    private ArrayList<String> lines;

    public static void main(String args[]) {
        Input input = new Input();
        input.run();
    }

    @Override
    public void run() {
        lines = new ArrayList<>();
        OptionReader.readOptions();
        storeLines();
        outputLines();
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
