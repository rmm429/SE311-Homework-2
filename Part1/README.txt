This part involves using Java interfaces for the Pipe and Filter.
When the program is started, it will prompt the user to enter lines that will be KWIC-sorted and stored for use.
Then, the user can search for a keyword that is stored within the KWIC sorted lines repository.

To run the program, run the Controller.java file in the "se311" folder.
The main() method is included here.
Concrete Filters can be arranged in any order in the Pipeline, and the Pipeline can handle one or more Filters.

Notes for grader:

	Each concrete Filter class each contains 8 identical helper methods.
    Two of these methods, determineInputType() and determineOutputType(), determine whether a Pipe will used or not for both input and output.
    If not using a Pipe, they will also determine whether to do console or file input/output.
    The other 6 methods are additional helpers that handle the console, file, and pipe input/output
    Each of the 6 methods mentioned above are called from the 2 aforementioned type-determining methods.
    These methods are identically included in all classes (except for Search) since the Filter class itself could not be changed.
    I structured it this way as to not include all of this code in each concrete Filter's run() method.
    The only Filter that differs in this regard is Search, which incorporates keyword highlighting when outputting to console.
    Furthermore, the console and file input methods for Search will prompt for a keyword rather than using the one in the config.properties file.

    The concrete Input and Output Filters are identical.
    This is because the Input Filter will handle output if there is no out Pipe and the Output Filter will accept input if there is no in Pipe.

config.properties:

	There is a config.properties file for each and every concrete Filter.
	These files can be found in the directory of each Filter implementation.

	Some of these configuration files differ to cater towards specific functionality of specific Filters.
	However, each file will have an option for the input/output type, input/output files, and punctuation remover.

	The fields 'InputType' and 'OutputType' can be set to "Console" and "File" to determine where the input comes from and output goes to.
	By default, these values are "File" and "Console" respectively, but can be changed based on the desired use case.

	The fields 'InputFileName' and 'OutputFileName' can be set to change what files each Filter reads from and writes to.
    By default, these values are "SampleInputFile.txt" anb "SampleOutputFile.txt" respectively.
    The output file does not need to exist prior to running the program, as it will be created if it does not exist and overwritten otherwise.
    However, the input file does need to exist prior to running the program, otherwise an error will occur.
    This is only relevant if file input/output is chosen.

    The field 'RemovePunct' will determine whether the filter will remove punctuation from the lines provided by the user.
    By default, this value is "true", but can be changed if you would like punctuation to be kept.
    See the "Case-sensitivity and punctuation" section below for more information.

	The above fields are only important for the first and last Filters used.
	The Filters in between the first and last ones in the Pipeline will use Pipes to transmit data.

	The last field, named 'Root', is where the input/output files will be obtained from.
	The value of this field should not be edited (unless the folders/paths containing these files is renamed or changed for some reason).

    The Stopword Remover confiuration file has an additional field named 'Stopwords'.
    This is a semicolon-separated list that contains stopwords that will be removed from the list of lines.
    Stopwords should be separated with a semicolon [;] in the stopwords list.
    This list of stopwords has a default value, but can be changed based on the desired use case.
    Furthermore, these words are case-insensitive and capitalization does not impact removal of a word.
    See the "Case-sensitivity and punctuation" section below for more information.

    The Search configuration file has an additional key named 'Keyword'.
    This is the keyword that will be searched for in the list of lines.
    The keyword has a default value, but can and should be changed based on the desired use case.
    The value set for this field is literal, meaning the Searcher will look for exactly whatever is entered.
    If getting input from the console or a file, the console will prompt the user to enter a keyword.
    This means that the 'Keyword' field only applies when Search gets input from a Pipe.
    Furthermore, the keyword is case-sensitive, meaning capitalization does matter.
    See the "Case-sensitivity and punctuation" section below for more information.

Case-sensitivity and punctuation:

	The alphabetizing sorter implemented in Sorter.java ignores case-sensitivity to sort the lines.
	This means that uppercase and lowercase letters are treated equally.

	Similarly, the stopword remover implemented in StopwordRemover.java also ignores case-sensitivity.
	This means, for example, including the word "is" in the stopword list will remove "is", "IS", "Is", and "iS".

	However, the search implementation in Search.java is case-sensitive.
	This means, for example, that searching for the keyword "hello" when lines contains "Hello" will not provide any results.

	Additionally, certain punctuation can be stripped from the lines provided by the user.
	This is determined by how the 'RemovePunct' field is set in each Filter's configuration file.
	This punctuation includes a period, comma, semicolon, question mark, and exclamation point [.,;?!].
	For example, "hello, how are you?" will be changed to "hello how are you".
	This also means that if a search is performed, this punctuation can be omitted while searching for a specific word.

Sources used to assist development:

	ArrayList removeIf() method: https://www.geeksforgeeks.org/arraylist-removeif-method-in-java/
	ArrayList to String: https://www.tutorialkart.com/java/how-to-join-elements-of-arraylist-with-delimiter-in-java/
