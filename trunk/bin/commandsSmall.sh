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
dataDir=${PWD}/data/$1/

if [ ! -d "$dataDir" ]; then
	echo "specifide bad dataSet. Dir $1 does not exist"
	exit 1
fi

echo "using dataset $dataDir"

java -jar ${LDAModelCreator}/target/LDAModelCreator-1.0-jar-with-dependencies.jar \
--trainingDir ${PWD}/smallMalletDir \
--malletFile ${PWD}/models/small/smallData.txt \


/home/kacper/dev/mallet/mallet-2.0.7/bin/mallet import-file \
--input ${PWD}/models/small/smallData.txt \
--token-regex '[\p{L}\p{M}]+'  \
--keep-sequence  \
--output ${PWD}/models/small/TrainingMoldel.mallet


/home/kacper/dev/mallet/mallet-2.0.7/bin/mallet train-topics  \
--input  ${PWD}/models/small/TrainingMoldel.mallet \
--num-topics 2 \
--num-iterations 2000 \
--inferencer-filename ${PWD}/models/small/Inferencer.mallet \
--output-topic-keys ${PWD}/models/small/smallDataKeys.txt \
--output-doc-topics ${PWD}/models/small/DocPerTopic.txt

