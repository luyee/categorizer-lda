#zakladamy ze jestesmy w katalogu glownym
set -e

if [ ! -n "$M_NUMITER" ]; then
  M_NUMITER=10000;
fi
if [ ! -n "$M_NUMTOPICS" ]; then
  M_NUMTOPICS=80;
fi


PWD=$(pwd)
baseN=$(basename $PWD)
malletHome="/home/kacper/dev/mallet/mallet-2.0.7"

echo $baseN
if [ "$baseN" != "categorizer-lda" ]; then
	echo "run form categorizer-lda direcotry"
	exit 1
fi

echo "wrapper script on mallet and malletmodelcreator"


InferencerOpt=${PWD}/InferencerOptymalizator
LDACreator=${PWD}/LDAModelCreator
dataDir=${PWD}/data/$1

if [ ! -d "$dataDir" ]; then
	echo "specifide bad dataSet. Dir $1 does not exist"
	exit 1
fi

echo "using dataset $dataDir"


echo "trained for ${M_NUMTOPICS} topics "

java -jar ${InferencerOpt}/target/InferencerOptymalizator-1.0-jar-with-dependencies.jar \
--inferencer ${dataDir}/Inferencer.mallet \
--training   ${dataDir}/TrainingMoldel.mallet \
--input      ${dataDir}/${1}Data.txt \
--docpertopic ${dataDir}/DocPerTopic.txt

echo "used ${dataDir}"
