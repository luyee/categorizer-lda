set -e
PWD=$(pwd)
echo $PWD
PWD=${PWD}/LDAModelCreator

java -jar ${PWD}/target/LDAModelCreator-1.0-jar-with-dependencies.jar \
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

