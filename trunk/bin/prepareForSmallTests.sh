#bin/bash
set -e
PWD=$(pwd)
dataDir=${PWD}/data/$1

if [ ! -d "$dataDir" ]; then
        echo "specifide bad dataSet. Dir $1 does not exist"
        exit 1
fi

echo "using dataset $dataDir"


echo $PWD
INFDIR=${PWD}"/Inferencer/src/main/resources"

echo $INFDIR

cp ${dataDir}/TrainingMoldel.mallet ${INFDIR}/TrainingMoldel.mallet 
cp ${dataDir}/Inferencer.mallet     ${INFDIR}/Inferencer.mallet
cp ${dataDir}/DocPerTopic.txt       ${INFDIR}/DocPerTopic.txt

mvn -f ${PWD}/Inferencer/pom.xml clean assembly:single install

