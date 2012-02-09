#java -jar ./target/LDAModelCreator-1.0-jar-with-dependencies.jar \
#--trainingDir ~/IdeaProjects/LDAModelCreator/allDatadir/ \
#--malletFile ~/IdeaProjects/LDAModelCreator/models/allData.txt \


/home/kacper/dev/mallet/mallet-2.0.7/bin/mallet import-file \
--input ./models/allData.txt \
--token-regex '[\p{L}\p{M}]+'  \
--keep-sequence  \
--output ./models/allData.mallet


/home/kacper/dev/mallet/mallet-2.0.7/bin/mallet train-topics  \
--input  ./models/allData.mallet \
--num-topics 80 \
--num-iterations 5000 \
--output-topic-keys ./models/allDataKeys.txt \
--output-doc-topics ./models/allDataDocTopic.txt

