#!/usr/bin/env bash

echo "exporting jars.."

cd $PLUGINS_HOME/plugins

for name1 in Core Claims ModTools Survival
do
cd $name1
mvn clean package
cd ../
wait
done

for name2 in Core Claims ModTools Survival
do
cp -f $name2/target/$name2-jar-with-dependencies.jar ../build/$name2.jar
wait
done

echo ""
read -n1 -r -p "Press any key to continue.." key