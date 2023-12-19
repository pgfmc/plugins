#! /usr/bin/bash

# The directory of the script no matter where it is being called from
# Differs from ${PWD}
#
# "In this example, we use the cd command to change the directory to the script’s
#  location, suppress any error messages with &> /dev/null, and then use the
#  pwd command to get the current directory."
#
# This is needed because we may want to call the script without needing to
# cd to the script first
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"

function main
{
	while [ true ]; do

		echo "Starting loop"

		# Create the startQ directory if it doesn't exist
		if [ ! -d "$SCRIPT_DIR/startQ" ]; then
			mkdir "$SCRIPT_DIR/startQ"
		else
			# Copy files from startQ
			echo "Moving plugins from StartQ..."
			find "$SCRIPT_DIR/startQ" -name "server.jar" -exec mv "{}" "$SCRIPT_DIR" \;
			find "$SCRIPT_DIR/startQ" -type f -exec mv "{}" "$SCRIPT_DIR/plugins" \;
			echo "Done!"
		fi

		echo ""

	    #echo "Updating Geyser and Floodgate..."
	    #wget "https://ci.opencollab.dev/job/GeyserMC/job/Geyser/job/master/lastSuccessfulBuild/artifact/bootstrap/spigot/build/libs/Geyser-Spigot.jar" -O "${PWD}/plugins/Geyser-Spigot.jar"
	    #wget "https://ci.opencollab.dev/job/GeyserMC/job/Floodgate/job/master/lastSuccessfulBuild/artifact/spigot/build/libs/floodgate-spigot.jar" -O "${PWD}/plugins/floodgate-spigot.jar"
	    #echo "Done!"
		#sleep 500ms
		#echo ""

		local java_args
		java_args=( -Xms2G -Xmx2G )

		if [ -f "$SCRIPT_DIR/java_args.txt" ]; then
			readarray -t java_args < "$SCRIPT_DIR/java_args.txt"
		else
			echo "WARNING: Generating default java_args.txt"
			echo "-Xms2G" > "$SCRIPT_DIR/java_args.txt"
			echo "-Xmx2G" >> "$SCRIPT_DIR/java_args.txt"
		fi

		echo "Java Arguments: ${java_args[@]}"

		java "${java_args[@]}" -jar "$SCRIPT_DIR/server.jar" nogui

		echo ""
	    echo "Server closed."
	    echo ""

		# Checks if the run_repo.sh script exists
		if [ ! -f "$SCRIPT_DIR/run_repo.sh" ]; then
			echo "WARNING: Generating default run_repo.sh"

			echo "cd $SCRIPT_DIR" > "$SCRIPT_DIR/run_repo.sh"
			echo "git add ." >> "$SCRIPT_DIR/run_repo.sh"
			echo "" >> "$SCRIPT_DIR/run_repo.sh"
			echo -e "git commit -m \"auto commit by server script\"" >> "$SCRIPT_DIR/run_repo.sh"
			echo "" >> "$SCRIPT_DIR/run_repo.sh"
			echo "git push" >> "$SCRIPT_DIR/run_repo.sh"
			echo "" >> "$SCRIPT_DIR/run_repo.sh"
		fi

		# Runs the script in a different process
		bash "$SCRIPT_DIR/run_repo.sh"


		IFS=''
		echo ""
		echo "Press [ESC] to quit the script."
		echo "Press [ENTER] to continue."
		echo ""

		for (( i=10; i>0; i--)); do
			echo "Starting in $i seconds..."

			local response
			read -s -N 1 -t 1 response

			if [ "$response" = $'\e' ]; then
			        echo -e "\nQuitting"
			        exit 0
			elif [ "$response" = $'\x0a' ]; then
			        break
			fi
		done

		echo ""



	done
}

main


