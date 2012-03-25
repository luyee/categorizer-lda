package ldainference;

import cc.mallet.pipe.*;
import cc.mallet.pipe.iterator.CsvIterator;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.types.InstanceList;
import interfaces.AbstractInferencer;
import interfaces.AbstractModelTrainer;

import java.io.*;
import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Pattern;

public class ModelTrainer extends AbstractModelTrainer {

    public static final double THRESHOLD = 0.0;
    public static final int MAX = -1;
    public static final String LDAMODEL_MALLET = "/LDAModel.mallet";
    private InstanceList trainingInstances;
    private final int numTopics;
    private final double alphaSum;
    private final double beta;
    private final int numIterations;
    private ParallelTopicModel model;
    private final String trainingCsvPath;
    private File docPerTopic;


    public ModelTrainer(String trainingCsvPath) {
        this(trainingCsvPath, 10000, 80);
    }

    public boolean readModelFromFile(String modelPath) throws Exception {
        String pathname = modelPath + LDAMODEL_MALLET;
        try {

            model = ParallelTopicModel.read(new File(pathname));
            this.modelFromFile = true;
            return true;
        } catch (Exception e) {
            System.err.println("Unable to restore saved topic model " +
                    pathname + ": " + e);
            return false;
        }
    }

    public void saveModel(String modelPath) {
        String pathname = modelPath + LDAMODEL_MALLET;
        assert (model != null);
        try {

            ObjectOutputStream oos =
                    new ObjectOutputStream(new FileOutputStream(pathname));
            oos.writeObject(model);
            oos.close();

        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Couldn't write topic model to filename " + model);
        }
    }

    public ModelTrainer(String trainingCsvPath, int numIterations, int numTopics) {
        this.trainingCsvPath = trainingCsvPath;
        this.numTopics = numTopics;
        alphaSum = 50.0;
        beta = 0.01;
        this.numIterations = numIterations;
        model = new ParallelTopicModel(numTopics, alphaSum, beta);
    }


    @Override
    public void trainModel() throws IOException, UnsupportedEncodingException {
        //String trainingCsvPath ="/home/kacper/IdeaProjects/LDAModelCreator/src/test/testsDir/UTFtest/engfile";
        // Begin by importing documents from text to feature sequences
        ArrayList<Pipe> pipeList = new ArrayList<Pipe>();

        // Pipes: lowercase, tokenize, remove stopwords, map to features
        pipeList.add(new CharSequenceLowercase());

        pipeList.add(new CharSequence2TokenSequence(Pattern.compile("[\\p{L}\\p{M}]+")));
        //pipeList.add( new TokenSequenceRemoveStopwords(new File("stoplists/en.txt"), "UTF-8", false, false, false) );
        pipeList.add(new TokenSequence2FeatureSequence());

        trainingInstances = new InstanceList(new SerialPipes(pipeList));

        Reader fileReader = new InputStreamReader(new FileInputStream(new File(trainingCsvPath)), "UTF-8");
        trainingInstances.addThruPipe(new CsvIterator(fileReader, Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$"),
                3, 2, 1)); // data, label, name fields

        // Create a model with 100 topics, alpha_t = 0.01, beta_w = 0.01
        //  Note that the first parameter is passed as the sum over topics, while
        //  the second is


        if (!this.modelFromFile) {
            model.setOptimizeInterval(0);
            model.setBurninPeriod(200);
            model.setSymmetricAlpha(false);

            model.addInstances(trainingInstances);

            // Use two parallel samplers, which each look at one half the corpus and combine
            //  statistics after every iteration.
            model.setNumThreads(2);

            // Run the model for 50 iterations and stop (this is for testing only,
            //  for real applications, use 1000 to 2000 iterations)
            model.setNumIterations(numIterations);
            model.estimate();
        }
        docPerTopic = File.createTempFile(UUID.randomUUID().toString(), ".docPerTopic");
        PrintWriter out = new PrintWriter(new FileWriter(docPerTopic));
        model.printDocumentTopics(out, THRESHOLD, MAX);
        out.close();
    }

    @Override
    public AbstractInferencer getInferencer() {
        return new Inferencer(docPerTopic, model.getInferencer(), trainingInstances);
    }
}