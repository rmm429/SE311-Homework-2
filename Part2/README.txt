cat InputFile.txt | ./run_input | ./run_remover | ./run_shifter | ./run_sorter | ./run_search | ./run_output > OutputFile.txt
OR
./run_input < InputFile.txt | ./run_remover | ./run_shifter | ./run_sorter | ./run_search | ./run_output > OutputFile.txt

-bash: OutputFile.txt: cannot overwrite existing file