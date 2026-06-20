./build.sh



for name in Core Claims ModTools Survival; do
    scp -i /home/james/.ssh/pgf target/"${name}".jar pgf@play.pgfmc.net:/pgf/proxied-test/test2/startQ/$name.jar
done

