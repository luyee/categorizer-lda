set -e

mvn -f Inferencer/pom.xml clean install assembly:single 
mvn -f InferencerOptymalizator/pom.xml clean package assembly:single 
#mvn -f LDAModelCreator/pom.xml clean package assembly:single
