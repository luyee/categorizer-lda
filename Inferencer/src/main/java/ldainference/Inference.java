package ldainference;

import cc.mallet.topics.TopicInferencer;
import cc.mallet.types.InstanceList;
import util.InputStremToTmpFile;

import java.io.*;

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


    public void rewriteTreiningFile(InstanceList previousInstanceList) throws IOException {
        //System.out.println(" Rewriting extended pipe from " + trainingFile.getName());
        //System.out.println("  Instance ID = " + previousInstanceList.getPipe().getInstanceId());

        ObjectOutputStream oos;

        oos = new ObjectOutputStream(new FileOutputStream(trainingFile));
        oos.writeObject(previousInstanceList);
        oos.close();
    }

    public void setInputFile(File file) {
        this.inputFile =file;
    }

    public File getDocPerTopic() {
        return docPerTopic;
    }

    public TopicInferencer getInferencer() {
        return inferencer;
    }

    public InstanceList getPreviousInstanceList() {
        return previousInstanceList;
    }

    public File getInputFile() {
        return inputFile;
    }
}
