#!/usr/bin/env bash


echo -e "\e[35mWorking in directory:" $PWD
echo -e "\e[31m--IMPORTANT--\e[0m"
echo -e "\e[31mThe above directory should match the directory in which the server.jar is.\e[0m"
echo -ne "Continue? [y/n] -> "
read response
if ! [[ $response = y ]]; then
    echo "Exiting..."
    exit 0
fi
response="n"

while [ true ]; do

    echo "Updating PGF plugins from StartQ..."

    # cp copies files
    cp -r "${PWD}/startQ/plugins" "${PWD}"

    # find command finds files (duh)
    # but it does more!
    #
    # the -exec flag allows a command to be run per file that matches the find.
    # here, it removes the file.
    # the -v (verbose) flag echoes each file removed.
    find "${PWD}/startQ/plugins" -type f -exec rm -v {} \;
    echo "Done!"

    echo "Updating Geyser and Floodgate..."
    wget "https://ci.opencollab.dev/job/GeyserMC/job/Geyser/job/master/lastSuccessfulBuild/artifact/bootstrap/spigot/build/libs/Geyser-Spigot.jar" -O "${PWD}/plugins/Geyser-Spigot.jar"
    wget "https://ci.opencollab.dev/job/GeyserMC/job/Floodgate/job/master/lastSuccessfulBuild/artifact/spigot/build/libs/floodgate-spigot.jar" -O "${PWD}/plugins/floodgate-spigot.jar"
    echo "Done!"

    sleep 500ms

    echo ""
    echo -e "\e[32mRunning Minecraft Server...\e[0m"
    java -Xms3000M -Xmx3000M -jar server.jar nogui
    sleep 10s
    echo "Server closed."
    echo ""

    echo "Restarting in 10 seconds..."
    echo -n "Do you want to cancel the script? [y/n] -> "

    read -t 10 response
    if [[ $response = y ]]; then
        echo "Exiting..."
        exit 0
    fi



done
