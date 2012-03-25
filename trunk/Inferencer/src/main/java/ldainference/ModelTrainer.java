package ldainference;

import cc.mallet.pipe.*;
import cc.mallet.pipe.iterator.CsvIterator;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.topics.TopicInferencer;
import cc.mallet.types.InstanceList;

import java.io.*;
import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Pattern;

public class ModelTrainer {

    public static final double THRESHOLD = 0.0;
    public static final int MAX = -1;
    private InstanceList instances;
    private final int numTopics;
    private final double alphaSum;
    private final double beta;
    private final int numIterations;
    private final ParallelTopicModel model;
    private final String trainingCsvPath;
    private File docPerTopic;

    public ModelTrainer(String trainingCsvPath, int numIterations) {
        this.trainingCsvPath = trainingCsvPath;
        numTopics = 2;
        alphaSum = 50.0;
        beta = 0.01;
        this.numIterations = numIterations;
        model = new ParallelTopicModel(numTopics, alphaSum, beta);
    }

    public TopicInferencer getInderencer(){
        return model.getInferencer();
    }

    public InstanceList getTrainingInstanceList(){
        return instances;
    }

    public File getDocPerTopic() {
        return docPerTopic;
    }

    public void trainModel() throws IOException, UnsupportedEncodingException {
        //String trainingCsvPath ="/home/kacper/IdeaProjects/LDAModelCreator/src/test/testsDir/UTFtest/engfile";
        // Begin by importing documents from text to feature sequences
        ArrayList<Pipe> pipeList = new ArrayList<Pipe>();

        // Pipes: lowercase, tokenize, remove stopwords, map to features
        pipeList.add(new CharSequenceLowercase());

        pipeList.add(new CharSequence2TokenSequence(Pattern.compile("[\\p{L}\\p{M}]+")));
        //pipeList.add( new TokenSequenceRemoveStopwords(new File("stoplists/en.txt"), "UTF-8", false, false, false) );
        pipeList.add(new TokenSequence2FeatureSequence());

        instances = new InstanceList(new SerialPipes(pipeList));

        Reader fileReader = new InputStreamReader(new FileInputStream(new File(trainingCsvPath)), "UTF-8");
        instances.addThruPipe(new CsvIterator(fileReader, Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$"),
                3, 2, 1)); // data, label, name fields

        // Create a model with 100 topics, alpha_t = 0.01, beta_w = 0.01
        //  Note that the first parameter is passed as the sum over topics, while
        //  the second is


        model.setOptimizeInterval(0);
        model.setBurninPeriod(200);
        model.setSymmetricAlpha(false);

        model.addInstances(instances);

        // Use two parallel samplers, which each look at one half the corpus and combine
        //  statistics after every iteration.
        model.setNumThreads(2);

        // Run the model for 50 iterations and stop (this is for testing only,
        //  for real applications, use 1000 to 2000 iterations)
        model.setNumIterations(numIterations);
        model.estimate();

        docPerTopic = File.createTempFile(UUID.randomUUID().toString(), ".docPerTopic");
        PrintWriter out = new PrintWriter (new FileWriter (docPerTopic));
        model.printDocumentTopics(out, THRESHOLD, MAX);
        out.close();
    }
}