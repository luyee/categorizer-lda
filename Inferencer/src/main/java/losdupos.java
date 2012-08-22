import evaluator.DataLoader;
import evaluator.Evaluator;
import evaluator.RaportWriterInterface;
import evaluator.SmallRaportWriter;
import interfaces.AbstractInferencer;
import interfaces.AbstractModelTrainer;
import svminferencer.KnnModelTrainer;
import svminferencer.WekaInferencer;
import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.File;
import java.util.UUID;
import java.util.Vector;

import static junit.framework.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: kacper
 * Date: 22/08/12
 * Time: 09:25
 * To change this template use File | Settings | File Templates.
 */
public class losdupos {

    public static void main(String[] args0) throws Exception {

        AbstractInferencer inferencer;
        KnnModelTrainer modelTrainer;
        WekaInferencer svmInferencer;

        String trainingFilePath = "/home/kacper/dev/lda/categorizer-lda/data/12/12Data.txt";
        String toInferencePath = "/home/kacper/dev/lda/categorizer-lda/data/12/12EvalData.txt";
        String savePath = File.createTempFile(UUID.randomUUID().toString(), "").getAbsolutePath();
        System.out.print(savePath);


        modelTrainer = new KnnModelTrainer(trainingFilePath,50,12);
        //modelTrainer.readModelFromFile(savePath);

        modelTrainer.trainModel();
//        modelTrainer.saveModel(savePath);

        Instances trainingInstances =modelTrainer.getWekaTrainingInstances();

        Attribute messageAtt1 = trainingInstances.attribute("Text");
        assertNotNull(messageAtt1);

        Instances testset = trainingInstances.stringFreeStructure();

        Classifier classifier = modelTrainer.getClassifier();

        // Create instance of length two.
        Instance instance = new Instance(2);

        // Set value for message attribute
        Attribute messageAtt = testset.attribute("Text");
        instance.setValue(messageAtt, messageAtt.addStringValue("0 1 2 3 0 1 2 3 0 1 2 3 0 1 2 3"));

        // Give instance access to attribute information from the dataset.
        instance.setDataset(testset);

        StringToWordVector filter = modelTrainer.getFilter();


        // Filter instance.
        filter.input(instance);
        Instance filteredInstance = filter.output();

        // Get index of predicted class value.
        double predicted = classifier.classifyInstance(filteredInstance);

        // Output class value.
        System.err.println("Message classified as : " +
                trainingInstances.classAttribute().value((int) predicted));

    }
}
