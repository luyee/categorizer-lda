package svminferencer;

import cc.mallet.pipe.*;
import cc.mallet.pipe.iterator.CsvIterator;
import cc.mallet.types.InstanceList;
import interfaces.AbstractInferencer;
import interfaces.AbstractModelTrainer;
import weka.classifiers.Classifier;
import weka.classifiers.functions.SMO;
import weka.core.Instances;

import java.io.*;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 3/24/12
 * Time: 7:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class SVMModelTrainer extends AbstractModelTrainer{

    public static final String SVMMODEL_WEKA = "/SVMModel.weka";
    protected File arrfTrainingFile;
    protected final InstanceListToArff instanceListToArff;
    protected  InstanceList trainingInstances;
    protected Classifier classifier;
    protected  Set<String> categoriesNames;
    protected final int numCategories;
    protected String trainingCsvPath;

    public SVMModelTrainer(String trainingCsvPath, int numCategories){
        this.modelFromFile=false;
        this.trainingCsvPath = trainingCsvPath;
        this.numCategories = numCategories;
        instanceListToArff = new InstanceListToArff();
        classifier = null;
    }




    public void  createArffTrainingFile() throws IOException {
        arrfTrainingFile = File.createTempFile(UUID.randomUUID().toString(), ".arff");
        arrfTrainingFile.deleteOnExit();
        //System.out.println("arf file : " + arrfTrainingFile.getAbsolutePath());
        PrintWriter pw = new PrintWriter(arrfTrainingFile);
        instanceListToArff.convert2ARFF(trainingInstances, pw, "trainingFile");
        categoriesNames = instanceListToArff.getCategoriesNames();
        pw.flush();
        pw.close();
    }


    @Override
    public void trainModel() throws Exception {
        Instances data = loadData();

        if (!modelFromFile){
            System.out.println("building");
            createClassifier(data);
        }


    }

    protected void createClassifier(Instances data) throws Exception {
        classifier = new SMO();
        classifier.buildClassifier(data);
    }

    protected Instances loadData() throws IOException {
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


        createArffTrainingFile();
        BufferedReader reader = new BufferedReader(
                new FileReader(arrfTrainingFile));
        Instances data = new Instances(reader);
        reader.close();
        data.setClassIndex(data.numAttributes() - 1);
        return data;
    }

    @Override
    public AbstractInferencer getInferencer() {
        return new SvmInferencer(trainingInstances,
                classifier,
                categoriesNames,
                numCategories);
    }

    @Override
    public boolean readModelFromFile(String modelPath) throws Exception{
        String pathname = modelPath + SVMMODEL_WEKA;
        try {

            ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(pathname));
            classifier = castToProprerClassifier(ois);
            ois.close();
            this.modelFromFile=true;
            return true;
        } catch (Exception e) {
            System.err.println("Unable to restore saved svn model " +
                    pathname     + ": " + e);
            return false;
        }
    }

    protected SMO castToProprerClassifier(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        return (SMO) ois.readObject();
    }

    @Override
    public void saveModel(String modelPath) {
        String pathname = modelPath + SVMMODEL_WEKA;
        assert (classifier != null);
        try {

            ObjectOutputStream oos =
                    new ObjectOutputStream (new FileOutputStream (pathname));
            oos.writeObject (classifier);
            oos.close();

        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException ("Couldn't write svm model to filename "+pathname);
        }
    }
}
