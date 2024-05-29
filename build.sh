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

for name in Core Claims ModTools Survival Proxycore-Velocity; do
	cd $name

	if ! [ -z "$version_number" ]; then
		sed -i "s/^version: .*/version: ${version_number}/" plugin.yml
	fi
	
	cd ../
done

cd "Maven"
mvn -e clean install
cd ../

wait

if ! [ -d "target" ]; then
    mkdir "target"
fi

for name in Core Claims ModTools Survival Proxycore-Velocity; do
    cp -f $name/target/$name.jar target/"${name^}".jar
done




echo ""
