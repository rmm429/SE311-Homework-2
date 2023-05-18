This part involves using standard input/output alongside individual programs/scripts for the Pipe and Filter (bash shell).
Beacause of this, individual programs/scripts were created, each handling a different purpose in the KWIC-Search pipeline.
These programs can be piped together and input/output redirection can be used.
Reader/"run_input" will prompt the user to enter lines that will be KWIC-sorted and stored for use.
The other programs besdies Search and Writer perform the KWIC sorting.
Search/"run_search" is where the user can search for a keyword that is stored within the KWIC sorted lines repository.
Writer/"run_output" is utilized to output the search results.
These programs can be run in different orders and each indiviudally can be utilize to execute different components of the KWIC sorting.

Executing the Filter programs:

	There are 6 scripts in this directory that will run the associated Filter code.
	These are:
		"run_input" for Reader
		"run_remover" for StopwordRemover
		"run_shifter" for Shifter
		"run_sorter" for Sorter
		"run_search" for Search
		"run_output" for Writer
	Each of these are associated with the folders of the same name without "run_" prepended (i.e. run_input -> "input" directory)
	Inside each of these folders are two additional scripts: "compile" and "run".
	"compile" will create the executable code needed for the root scripts to work.
	"run" will run the specific Filter individually, and is not needed since the root scripts allow for running and piping.
	NOTE: If you open these Filters using IntelliJ, make sure to open the specific folder of each Filter.
	Do not open the "Part2" folder in IntelliJ; rather, open "input", "search", "output", etc.

	Each Filter needs to be compiled, which can be done with the "make" script in this directory.
	To compile all of the Filters, run:
		./make
	See the "Potential errors" section below if running this script is unsuccessful.

	These scripts can be executed standalone, for example:
		./run_input
	Additionally, they can be piped together one at a time, for example
		./run_input | ./run_output
	Furtermore, all of the scripts can be piped together, like so:
		./run_input | ./run_remover | ./run_shifter | ./run_sorter | ./run_search | ./run_output
	The order in which you place the scripts in the pipeline matters, as each will be run in the order provided.

	When there is no input or output redirection, the user can provide input and output using the console.
	In this case, input can be provided using stdin in the console.
	Press "Enter"/"Return" to separate your provided lines.
	When you are done providing input, sending an EOF signal will terminate input.
	This can be done with "CTRL+D" on most systems, however Mac might be "CMD+D".
	Once this signal is received, stdout will output to the console right below the input provided.

	Redirection can also be used with these scripts.
	Scripts can receive input from a file, for example:
		cat InputFile.txt | ./run_input
	OR
		./run_input < InputFile.txt
	Additonally, scripts can output to a file, for example:
		./run_output > OutputFile.txt
	Both of these can be used at the same time.
	This also works with pipes, for example:
		cat InputFile.txt | ./run_input | ./run_remover | ./run_shifter | ./run_sorter | ./run_search | ./run_output > OutputFile.txt
	OR
		./run_input < InputFile.txt | ./run_remover | ./run_shifter | ./run_sorter | ./run_search | ./run_output > OutputFile.txt
	When using redirection, the console will not prompt for input and/or display output.
	See the "Potential errors" section below for output redirection.

Potential errors:

	Execution privileges are needed for the "make" script.
	If you get the error "-bash: ./make: Permission denied", run:
		chmod +x make
	The "make" script will automatically set execution privileges for the other scripts when run.

	Running the "make" script may result in carriage return errors.
	This is because I developed them on Windows, which has some endline anomalies within Unix.
	There is a Windows-to-Unix carriage return converter program called "dos2unix" that will fix this.
	If you get an error similar to "compile: line 2: $'\r': command not found" when executing "make", run the script "convert":
		./convert
	"dos2unix" may not be installed, which will be shown by getting an error similar to "dos2unix: command not found".
	Your local environment should give you instructions on how to install this program.
	If not, installation can be done on Ubuntu/Debian by running:
		sudo apt install dos2unix
	This is only necessary if you get the "\r" error mentioned above.

	If there are any persisting errors while running the "make" script not described here, the "compile" script in each directory will have to be run.
	Move/"ls" into each directory and run "./compile" to do so.
	This is not ideal, which is why the "make" script was created, but necessary if it does not work.

	If redirecting to an output file that already exists, an overwrite error may occur.
	This will look something similar to "-bash: OutputFile.txt: cannot overwrite existing file".
	This is a limitation of Unix redirection, and the existing output file will have to be deleted before it can be written to again.

config.properties:

	There is a config.properties file for each and every Filter.
	These files can be found in the directory of each Filter implementation.

	Some of these configuration files differ to cater towards specific functionality of specific Filters.
	However, each file will have an option for the punctuation remover.

    The field 'RemovePunct' will determine whether the filter will remove punctuation from the lines provided by the user.
    By default, this value is "true", but can be set to "false" if you would like punctuation to be kept.
    It is important to note that if even just one Filter has this field set to "true" and all Filters are being used/piped, punctuation will still be removed.
    To keep punctuation when using multiple Filters, make sure that this field is set to "false" in each of those filters.
    See the "Case-sensitivity and punctuation" section below for more information.

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

	String to ArrayList: https://www.javadevjournal.com/java/arraylist-addall-method/
	ArrayList removeIf() method: https://www.geeksforgeeks.org/arraylist-removeif-method-in-java/
