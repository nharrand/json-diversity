#!/bin/zsh

PATH_TO_BENCH="/home/nharrand/Documents/depswap/data/bench/"
CATEGORY="undefined"
JAR="target/depswap-test-bench-0.1-SNAPSHOT-jar-with-dependencies.jar"
PARSER="jjson"
RESULT="results/${PARSER}_${CATEGORY}_results.csv"


for f in $(ls $PATH_TO_BENCH/$CATEGORY)
do
	timeout 15s java -cp $JAR se.kth.assertteam.jsonbench.CLIMode $f
	if [ $? -eq 124 ]; then
		echo "timout for $f"
		echo "$PARSER,$CATEGORY,$f,CRASH" >> $RESULT
	else
		echo "ok for $f"
	fi
done
