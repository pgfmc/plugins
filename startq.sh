#!/usr/bin/env sh



echo ""
echo "Make sure this script is running in the same folder as the server.jar."


working_directory= | pwd
echo $working_directory

echo ""

# Code for the backup
if [ $# = 1 ]; then
    echo "Starting backup into "$1"..."
    #put code for backup here...

# -gt | Greater than
elif [ $# -gt 1 ]; then
    echo "Too many Arguments!"
    echo "Correct usage: $ "$0" <backup dir>"
    echo "Starting backup into "$1"..."

    # Does the backup anyways, if $1 is a directory.
else
    echo "No backup directory supplied, moving on..."

fi

# the pipe | feeds the output of pwd (gets the current directory) and feeds it into the
# command before it.




mv -rvi "${working_directory/startQ/plugins}" "${working_directory/plugins}"
