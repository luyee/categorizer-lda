#zakladamy ze jestesmy w katalogu glownym
set -e


PWD=$(pwd)
baseN=$(basename $PWD)

echo $baseN
if [ "$baseN" != "categorizer-lda" ]; then
	echo "run form categorizer-lda direcotry"
	exit 1
fi

echo "wrapper script on mallet and malletmodelcreator"


LDACreator=${PWD}/LDAModelCreator
dataDir=${PWD}/data/$1

if [ ! -d "$dataDir" ]; then
	echo "specifide bad dataSet. Dir $1 does not exist"
	exit 1
fi

echo "using dataset $dataDir"

java -jar ${LDACreator}/target/LDAModelCreator-1.0-jar-with-dependencies.jar \
--trainingDir ${dataDir}/${1}EvalDir \
--malletFile ${dataDir}/${1}EvalData.txt \
--convert