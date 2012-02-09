#bin/bash
set -e
PWD=$(pwd)
echo $PWD
SMALLDIR=${PWD}"/LDAModelCreator/models/small"
INFDIR=${PWD}"/Inferencer/src/main/resources"

echo $SMALLDIR
echo $INFDIR

sh ./LDAModelCreator/commandsSmall.sh
cp ${SMALLDIR}/TrainingMoldel.mallet ${INFDIR}/TrainingMoldel.mallet 
cp ${SMALLDIR}/Inferencer.mallet     ${INFDIR}/Inferencer.mallet
cp ${SMALLDIR}/DocPerTopic.txt       ${INFDIR}/DocPerTopic.txt

mvn -f ${PWD}/Inferencer/pom.xml clean assembly:single install

