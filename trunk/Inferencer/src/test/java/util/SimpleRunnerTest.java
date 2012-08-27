package util;

import evaluator.DataLoader;
import evaluator.Evaluator;
import interfaces.AbstractInferencer;
import interfaces.AbstractModelTrainer;
import ldainference.ModelTrainer;
import org.apache.log4j.Logger;
import org.junit.Test;
import svminferencer.KnnModelTrainer;
import svminferencer.SVMModelTrainer;
import svminferencer.WekaInferencer;

import java.io.File;
import java.util.Arrays;
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
    private Logger logger = Logger.getLogger(SimpleRunnerTest.class);

    @Test
    public void testMainKNN() throws Exception {

        AbstractModelTrainer modelTrainer;
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

        logger.debug(Arrays.toString(evaluator.getCorrect()));

        for (int i=0;i<correct.length;i++){
            assertEquals(correct[i], evaluator.getCorrect()[i],0.00001);
        }
    }

    @Test
    public void testMainSVN() throws Exception {

        AbstractModelTrainer modelTrainer;
        String trainingFilePath = "/home/kacper/dev/lda/categorizer-lda/data/12/12Data.txt";
        String toInferencePath = "/home/kacper/dev/lda/categorizer-lda/data/12/12EvalData.txt";
        String savePath = File.createTempFile(UUID.randomUUID().toString(), "").getAbsolutePath();
        System.out.print(savePath);
        modelTrainer = new SVMModelTrainer(trainingFilePath,12);
        //modelTrainer.readModelFromFile(savePath);

        modelTrainer.trainModel();
//        modelTrainer.saveModel(savePath);


        DataLoader dataLoader = new DataLoader(toInferencePath);
        Vector<String> data = dataLoader.getEvaluationInsatnces();
        Evaluator evaluator = new Evaluator(data,modelTrainer.getInferencer());
        evaluator.evaluate();

        double [] correct= {0.4107142857142857, 0.5892857142857143, 0.5892857142857143, 0.7142857142857143, 0.75, 0.8571428571428571};

        logger.debug(Arrays.toString(evaluator.getCorrect()));
        for (int i=0;i<correct.length;i++){
            assertEquals(correct[i], evaluator.getCorrect()[i],0.00001);
        }
    }


    @Test
    public void testMainLDA() throws Exception {

        AbstractModelTrainer modelTrainer;
        String trainingFilePath = "/home/kacper/dev/lda/categorizer-lda/data/12/12Data.txt";
        String toInferencePath = "/home/kacper/dev/lda/categorizer-lda/data/12/12EvalData.txt";
        String savePath = File.createTempFile(UUID.randomUUID().toString(), "").getAbsolutePath();
        System.out.print(savePath);
        modelTrainer = new ModelTrainer(trainingFilePath,200,12);


        //modelTrainer.readModelFromFile(savePath);

        modelTrainer.trainModel();
//        modelTrainer.saveModel(savePath);


        DataLoader dataLoader = new DataLoader(toInferencePath);
        Vector<String> data = dataLoader.getEvaluationInsatnces();
        Evaluator evaluator = new Evaluator(data,modelTrainer.getInferencer());
        evaluator.evaluate();

        logger.debug(Arrays.toString(evaluator.getCorrect()));

        double correct[] = {0.5714285714285714, 0.6964285714285714, 0.75,
                0.8571428571428571, 0.9285714285714286, 1.0};


        for (int i=1;i<correct.length;i++){
            assertTrue( evaluator.getCorrect()[i]>0.5);
        }

        assertEquals(evaluator.getCorrect()[correct.length-1],1.0);
    }

}
