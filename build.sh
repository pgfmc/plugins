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

for name in core claims modtools survival; do
	cd $name

	if ! [ -z "$version_number" ]; then
		sed -i "s/^version: .*/version: ${version_number}/" plugin.yml
	fi
	
	cd ../
done

cd "./Maven"
mvn -e clean install
cd ../

wait

for name in core claims modtools survival; do
	cp -f $name/target/$name.jar ../.build/"${name^}".jar
done

wait

echo ""
read -n1 -r -p "Press any key to continue.." key