#!/usr/bin/env bash

echo "Input version or return for unchanged: "
read -r version_number

if [ -z "$version_number" ]; then
	echo "Version is unchanged"
else
	echo "New version is ${version_number}"
fi

echo ""
sleep 1s
echo "Exporting jars.."

cd $PLUGINS_HOME/plugins

for name in Core Claims ModTools Survival; do
	cd $name

	if ! [ -z "$version_number" ]; then
		sed -i "s/^version: .*/version: ${version_number}/" plugin.yml
	fi

	mvn clean package
	cd ../
	wait
done

for name in Core Claims ModTools Survival; do
	cp -f $name/target/$name-jar-with-dependencies.jar ../build/$name.jar
	wait
done

echo ""
read -n1 -r -p "Press any key to continue.." key