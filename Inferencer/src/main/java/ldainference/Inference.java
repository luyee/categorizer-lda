package ldainference;

import DistClasificator.Category;
import DistClasificator.DistanceClassifier;
import cc.mallet.pipe.*;
import cc.mallet.pipe.iterator.CsvIterator;
import cc.mallet.topics.TopicInferencer;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import util.InputStremToTmpFile;
import util.MalletWriter;

import java.io.*;
import java.util.Vector;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 2/7/12
 * Time: 5:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class Inference {


    private File outputFile;
    private File trainingFile;
    private File inferenceFile;

    private File inputFile;
    private File docPerTopic=null;

    private final InputStremToTmpFile inputStremToTmpFile = new InputStremToTmpFile();
    private InstanceList previousInstanceList;
    private final TopicInferencer inferencer;

    public Inference(File trainingFile, File inferenceFile, File docPerTopic) throws Exception {
        this.trainingFile = trainingFile;
        this.inferenceFile = inferenceFile;
        this.docPerTopic = docPerTopic;
        previousInstanceList = InstanceList.load(this.trainingFile);
        inferencer = TopicInferencer.read(this.inferenceFile);
    }

    
    
    public Inference(InstanceList previousInstanceList, TopicInferencer inferencer1, File docPerTopic1){
        this.previousInstanceList = previousInstanceList;
        this.inferencer = inferencer1;
        this.docPerTopic = docPerTopic1;
    }
    


    public Inference() throws Exception {
        buildFromResources();
        inferencer = TopicInferencer.read(this.inferenceFile);
    }
    public Inference(File outputFile, File inputFile) throws Exception {
        this.outputFile = outputFile;
        this.inputFile = inputFile;
        buildFromResources();
        inferencer = TopicInferencer.read(this.inferenceFile);
    }
    


    public Inference(File trainingFile, File outputFile, File inferenceFile, File inputFile) throws Exception {
        this.trainingFile = trainingFile;
        this.outputFile = outputFile;
        this.inferenceFile = inferenceFile;
        this.inputFile = inputFile;
        previousInstanceList = InstanceList.load(this.trainingFile);
        inferencer = TopicInferencer.read(this.inferenceFile);
    }

    public void close() throws IOException {
        rewriteTreiningFile(previousInstanceList);
    }

    private File convertToTmpFile(InputStream inputStream) throws IOException {

        return inputStremToTmpFile.convertToTmpFile(inputStream);
    }

    
    
    public void buildFromResources() throws IOException {


        InputStream inputStreamInferencer =
                this.getClass().getClassLoader().getResourceAsStream("Inferencer.mallet");

        this.inferenceFile = inputStremToTmpFile.convertToTmpFile(inputStreamInferencer);

        InputStream inputStreamTraining =
                this.getClass().getClassLoader().getResourceAsStream("TrainingMoldel.mallet");

        this.trainingFile = inputStremToTmpFile.convertToTmpFile(inputStreamTraining);
        previousInstanceList = InstanceList.load(this.trainingFile);

    }


    public Vector<String> inferenceCategories(Vector<String> instance) throws Exception {
        this.inputFile = MalletWriter.addToCvs(instance);
        InstanceList instances = createInstances();
        Category cats = infCategories(instances).get(0);
        DistanceClassifier distanceClassifier= new DistanceClassifier();
        if (docPerTopic==null){
        InputStream inputStreamTraining =
                this.getClass().getClassLoader().getResourceAsStream("DocPerTopic.txt");

        distanceClassifier.readDocTopicasStream(inputStreamTraining);
        }else {
            distanceClassifier.readDocTopicasFile(docPerTopic);
        }
        return distanceClassifier.classifyStr(cats);

    }
    
    private Vector<Category> infCategories(InstanceList instanceList) throws Exception {


        InstanceList instances = instanceList;

        int numIterations = 100;
        int sampleInterval = 10;
        int burnInIterations = 10;

        Vector<Category> vector = new Vector<Category>();
        for (Instance instance : instances) {

            double[] topicDistribution =
                    inferencer.getSampledDistribution(instance, numIterations,
                            sampleInterval, burnInIterations);
            vector.add(new Category(topicDistribution));
        }
        return vector;

    }

    public void inference( InstanceList instanceList) {

        try {

            TopicInferencer inferencer =
                    TopicInferencer.read(this.inferenceFile);

            InstanceList instances = instanceList;

            int numIterations = 100;
            int sampleInterval = 10;
            int burnInIterations = 10;
            double docTopicsThreshold = 0.0;
            int docTopicsMax = 20;
            inferencer.writeInferredDistributions(instances, this.outputFile,
                    numIterations, sampleInterval,
                    burnInIterations,
                    docTopicsThreshold, docTopicsMax);


        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }


    public InstanceList createInstances() throws IOException, UnsupportedEncodingException {


        Pipe instancePipe;

        instancePipe = previousInstanceList.getPipe();

        //
        // Create the instance list and open the input file
        //
        InstanceList instances = new InstanceList(instancePipe);
        Reader fileReader;

        fileReader = new InputStreamReader(new FileInputStream(this.inputFile), "UTF-8");


        instances.addThruPipe(new CsvIterator(fileReader, Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$"),
                3, 2, 1));

        return instances;

    }

    public void rewriteTreiningFile(InstanceList previousInstanceList) throws IOException {
        //System.out.println(" Rewriting extended pipe from " + trainingFile.getName());
        //System.out.println("  Instance ID = " + previousInstanceList.getPipe().getInstanceId());

        ObjectOutputStream oos;

        oos = new ObjectOutputStream(new FileOutputStream(trainingFile));
        oos.writeObject(previousInstanceList);
        oos.close();
    }

}
