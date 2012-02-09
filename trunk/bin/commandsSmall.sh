#zakladamy ze jestesmy w katalogu glownym
set -e

if [ ! -n "$M_NUMITER" ]; then
  M_NUMITER=1000;
fi
if [ ! -n "$M_NUMTOPICS" ]; then
  M_NUMTOPICS=2;
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


LDACreator=${PWD}/LDAModelCreator
dataDir=${PWD}/data/$1

if [ ! -d "$dataDir" ]; then
	echo "specifide bad dataSet. Dir $1 does not exist"
	exit 1
fi

echo "using dataset $dataDir"

java -jar ${LDACreator}/target/LDAModelCreator-1.0-jar-with-dependencies.jar \
--trainingDir ${dataDir}/${1}Dir \
--malletFile ${dataDir}/${1}Data.txt \


${malletHome}/bin/mallet import-file \
--input ${dataDir}/${1}Data.txt \
--token-regex '[\p{L}\p{M}]+'  \
--keep-sequence  \
--output ${dataDir}/TrainingMoldel.mallet


${malletHome}//bin/mallet train-topics  \
--input  ${dataDir}/TrainingMoldel.mallet \
--num-topics ${M_NUMTOPICS} \
--num-iterations ${M_NUMITER} \
--inferencer-filename ${dataDir}/Inferencer.mallet \
--output-topic-keys ${dataDir}/DataKeys.txt \
--output-doc-topics ${dataDir}/DocPerTopic.txt

echo "trained for ${M_NUMTOPICS} topics "

