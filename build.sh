#!/usr/bin/env sh



# The below section determines which plugins to compile
# I didnt like waiting all the time when i only wanted to compile Parkour,
# so i added this.
#
# just do ./build.sh <plugin> and itll work perfectly
# until it doesnt of course.

#echo $#

cd "$PLUGINS_HOME/plugins"
if ! [ $PWD == "$PLUGINS_HOME/plugins" ]; then
	echo "CD into plugins failed!"
	echo "current working directory:"
	echo $PWD
   exit 1
fi

echo "Input version or return for unchanged: "
read -r version_number

if [ -z "$version_number" ]; then
	echo "Version is unchanged"
else
	echo "New version is ${version_number}"
fi

if [ $# -gt 0 ]; then
	compiling_plugins=$@
else
	compiling_plugins=("Core" "Claims" "ModTools" "Survival" "Parkour")
fi

echo "Exporting jars.."
for name in ${compiling_plugins[@]}; do
	cd $name

	if ! [ -z "$version_number" ]; then
		sed -i "s/^version: .*/version: ${version_number}/" plugin.yml
	fi

	mvn clean install
	cd ../
	wait
done

echo "copying jars into build..."
sleep 1s

for name in ${compiling_plugins[@]}; do
	cp -f $name/target/$name-jar-with-dependencies.jar ../build/$name.jar
	wait
done

echo "All Finished!"
