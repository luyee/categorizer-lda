package ldainference;

import DistClasificator.Category;
import DistClasificator.DistanceClassifier;
import cc.mallet.pipe.Pipe;
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
    
    public Inference(File trainingFile, File inferenceFile, File docPerTopic) {
        this.trainingFile = trainingFile;
        this.inferenceFile = inferenceFile;
        this.docPerTopic = docPerTopic;
    }


    public Inference() throws IOException {
        buildFromResources();
    }
    public Inference(File outputFile, File inputFile) throws IOException {
        this.outputFile = outputFile;
        this.inputFile = inputFile;
        buildFromResources();
    }

    public Inference(File trainingFile, File outputFile, File inferenceFile, File inputFile) {
        this.trainingFile = trainingFile;
        this.outputFile = outputFile;
        this.inferenceFile = inferenceFile;
        this.inputFile = inputFile;
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


        TopicInferencer inferencer =
                TopicInferencer.read(this.inferenceFile);

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
        InstanceList previousInstanceList = null;


        previousInstanceList = InstanceList.load(this.trainingFile);
        instancePipe = previousInstanceList.getPipe();


        //
        // Create the instance list and open the input file
        //
        InstanceList instances = new InstanceList(instancePipe);
        Reader fileReader;

        fileReader = new InputStreamReader(new FileInputStream(this.inputFile), "UTF-8");

        // 
        // Read instances from the file
        //

        instances.addThruPipe(new CsvIterator(fileReader, Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$"),
                3, 2, 1));


        System.out.println(" Rewriting extended pipe from " + trainingFile.getName());
        System.out.println("  Instance ID = " + previousInstanceList.getPipe().getInstanceId());

        ObjectOutputStream oos;

        oos = new ObjectOutputStream(new FileOutputStream(trainingFile));
        oos.writeObject(previousInstanceList);
        oos.close();


        return instances;

    }

}
