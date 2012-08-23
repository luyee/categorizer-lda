package util;

import evaluator.DataLoader;
import evaluator.Evaluator;
import interfaces.AbstractInferencer;
import interfaces.AbstractModelTrainer;
import org.junit.Test;
import svminferencer.KnnModelTrainer;
import svminferencer.WekaInferencer;

import java.io.File;
import java.util.UUID;
import java.util.Vector;

import static junit.framework.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: kacper
 * Date: 23/08/12
 * Time: 12:34
 * To change this template use File | Settings | File Templates.
 */

public class SimpleRunnerTest {
    @Test
    public void testMain() throws Exception {

        AbstractInferencer inferencer;
        AbstractModelTrainer modelTrainer;
        WekaInferencer svmInferencer;

        String trainingFilePath = "/home/kacper/dev/lda/categorizer-lda/data/12/12Data.txt";
        String toInferencePath = "/home/kacper/dev/lda/categorizer-lda/data/12/12EvalData.txt";
        String savePath = File.createTempFile(UUID.randomUUID().toString(), "").getAbsolutePath();
        System.out.print(savePath);


        modelTrainer = new KnnModelTrainer(trainingFilePath,50,12);
        //modelTrainer.readModelFromFile(savePath);

        modelTrainer.trainModel();
//        modelTrainer.saveModel(savePath);


        DataLoader dataLoader = new DataLoader(toInferencePath);
        Vector<String> data = dataLoader.getEvaluationInsatnces();
        Evaluator evaluator = new Evaluator(data,modelTrainer.getInferencer());
        evaluator.evaluate();

        double [] correct = {0.17857142857142858, 0.25, 0.39285714285714285,
                0.39285714285714285, 0.39285714285714285, 0.39285714285714285};

        for (int i=0;i<correct.length;i++){
            assertEquals(correct[i], evaluator.getCorrect()[i],0.00001);
        }
    }
}
