
#### Unix
`sudo su` change to root user <br/>
`history`  gives a history of commands run <br/>
`!number`  repeats command run on the line number <br/>
`ctrl + c ` halt current command (do not execute) <br/>
`ctrl + z ` suspend process <br/>
`ctrl + d `  log out of current session <br/>
`ctrl + w`  erases one word in the current line <br/>
`ctrl + u`  erases the whole line <br/>
`ctrl + l` clear screen <br/>
`!!`  repeats the last command <br/>
`ps` display currently active processes <br/>
`top` display all running processes <br/>
`kill -9 id <pid>` kill process id pid <br/>
`bg` lists stopped or background jobs; resume a stopped job in the background <br/>
`fg` brings the most recent job to the foreground <br/>
`fg n` brings job n to the foreground <br/>
`ls -F` adds the flag option which tells ls to classify the output based on data type <br/>
`ls --help` tells you which options a command takes <br/>
`ls -t` or `l -t` lists items by time of last change <br/>
`wc -l` shows the number of lines per file <br/>
`cat` concatenate, prints the contents of multiple files one after another <br/>
`head` retrieves the first few lines of a file <br/>
`head -n 20` retrieves the first twenty lines of code <br/>
`tail` retrieves the last few lines of code <br/>
`grep` search a file for a specific word ex: `grep knot haiku.txt` <br/>
`grep -w` limits search to a specific phrase `grep -w “is knot” haiku.txt` <br/>
`grep -n` numbers the lines that the text is found in <br/>
`grep -i` makes the search non case sensitive <br/>
`grep -l` lists file names only <br/>
`grep -r “knot” *` reads files under each directory recursively <br/>
`grep -rni “knot” *` recursive, display line numbers, non case sensitive <br/>
`find .` displays every file and directory below <br/>
`find . -type d` displays only directories below <br/>
`find . -type f` displays only files below <br/> 
`find . -name “*.txt”` finds all text files <br/>
`find . -name Dockerfile *` find all files with a specific name below <br/>
`ls -l <filename>` view the permissions of a file <br/>
`ls -al` view the permissions of all files in a directory <br/>
`id <username>` find UID and group membership of a user <br/>
`id -u <username>` find the UID (user ID) of a user <br/>
`id -Gn <username>` or `id -G` to find the groups of a user <br/>
`id -gn <username>` or `id -g` to find the current group of a user <br/>
`chown <username>:<usergroup> list.html` to change file ownership <br/>
`free -h` find the amount of memory/memory usage <br/>
`lscpu` on linux find the number of cores/threads available <br/>
`sysctl -a | grep machdep.cpu` on macos find the number of cores/threads available </br/>
`df -h` see storage across the entire filesystem <br/>
`df -Ph . | tail -1 | awk '{print $4}'` see storage available to the current directory <br/>
