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

import static org.apache.commons.io.FileUtils.copyFile;

public class ModelTrainer extends AbstractModelTrainer {

    public static final double THRESHOLD = 0.0;
    public static final int MAX = -1;
    private InstanceList trainingInstances;
    private final int numTopics;
    private final double alphaSum;
    private final double beta;
    private final int numIterations;
    private ParallelTopicModel model;
    private final String trainingCsvPath;
    private File docPerTopic;

    /**
     * Simplified Constructor equivalent to calling
     * ModelTrainer(rainingCsvPath, 10000, 80)
     */
    public ModelTrainer(String trainingCsvPath) {
        this(trainingCsvPath, 10000, 80);
    }

    /**
     * This method is reading model from 3 files which prefixes
     * are described by pathname.
     * pathname +"model.lda" is path for trained model
     * pathname +"trainignInstaces.lda" is path for training instances in binary format
     * pathname + "docPerTopic.lda" is path for model for for KNN like second model
     * for more information about how these are used please consult readme attached
     *
     * @param pathname path defining location of model
     */
    public void readModelFromFile(String pathname) throws Exception {

        try {

            model = ParallelTopicModel.read(new File(pathname + "model.lda"));
            ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(pathname + "trainignInstaces.lda"));
            trainingInstances = (InstanceList) ois.readObject();
            ois.close();
            docPerTopic = new File(pathname + "docPerTopic.lda");
            this.modelFromFile = true;

        } catch (Exception e) {
            System.err.println("Unable to restore saved topic model " +
                    pathname + ": " + e);

        }
    }


    /**
     * This method is saving model to 3 files which prefixes
     * are described by pathname.
     * pathname +"model.lda" is path for trained model
     * pathname +"trainignInstaces.lda" is path for training instances in binary format
     * pathname + "docPerTopic.lda" is path for model for for KNN like second model
     * for more information about how these are used please consult readme attached
     *
     * @param pathname path definig location of model
     */
    public void saveModel(String pathname) {

        assert (model != null && trainingInstances != null && docPerTopic != null);

        try {

            ObjectOutputStream oos =
                    new ObjectOutputStream(new FileOutputStream(pathname + "model.lda"));
            oos.writeObject(model);
            oos.close();
            oos = new ObjectOutputStream(new FileOutputStream(pathname + "trainignInstaces.lda"));
            oos.writeObject(trainingInstances);
            oos.close();
            copyFile(docPerTopic,
                    new File(pathname + "docPerTopic.lda"));

        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Couldn't write topic model to filename " + model);
        }
    }

    /**
     * Basic Constructor
     *
     * @param trainingCsvPath path for training instances which format is described in attached readme
     * @param numIterations   how many iterations perform while learning model (10000 is almost for sure O.K)
     * @param numTopics       number of internal topics ( abstract topics NOT the categories - in other words
     *                        number of dimensions we want to reduce ours space to ). Setting this number to number of categories is usual
     *                        strategy.
     */
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

        // Begin by importing documents from text to feature sequences
        ArrayList<Pipe> pipeList = new ArrayList<Pipe>();

        pipeList.add(new CharSequence2TokenSequence(Pattern.compile("[\\p{L}\\p{M}]+")));

        pipeList.add(new TokenSequence2FeatureSequence());

        trainingInstances = new InstanceList(new SerialPipes(pipeList));

        Reader fileReader = new InputStreamReader(new FileInputStream(new File(trainingCsvPath)), "UTF-8");
        trainingInstances.addThruPipe(new CsvIterator(fileReader, Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$"),
                3, 2, 1)); // data, label, name fields


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