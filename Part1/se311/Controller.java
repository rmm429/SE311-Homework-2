package se311;

import se311.helpers.*;
import se311.input.Input;
import se311.output.Output;
import se311.remover.StopwordRemover;
import se311.search.Search;
import se311.shifter.Shifter;
import se311.sorter.Sorter;


public class Controller {
	public static void main(String[] args) {

 // At least two filters are needed
    (new Pipeline(
			new Input(),
			new Shifter(),
			new StopwordRemover(),
			new Sorter(),
			new Output()
		)).run();
	}
}
