import evaluator.DataLoader;
import evaluator.Evaluator;
import evaluator.RaportWriterInterface;
import evaluator.SmallRaportWriter;
import interfaces.AbstractInferencer;
import interfaces.AbstractModelTrainer;
import ldainference.ModelTrainer;
import org.apache.log4j.Logger;
import svminferencer.KnnModelTrainer;
import svminferencer.WekaInferencer;

import java.io.File;
import java.util.Arrays;
import java.util.UUID;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 3/25/12
 * Time: 11:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleRunner {


    private static Logger logger = Logger.getLogger(SimpleRunner.class);

    public static void main(String[] args0) throws Exception {


        AbstractInferencer inferencer;
        AbstractModelTrainer modelTrainer;
        WekaInferencer svmInferencer;

        String trainingFilePath = "/home/kacper/dev/lda/categorizer-lda/data/12/12Data.txt";
        String toInferencePath = "/home/kacper/dev/lda/categorizer-lda/data/12/12EvalData.txt";
        String savePath = File.createTempFile(UUID.randomUUID().toString(), "").getAbsolutePath();
        System.out.print(savePath);


        modelTrainer = new KnnModelTrainer(trainingFilePath, 50, 12);
        //modelTrainer.readModelFromFile(savePath);

        modelTrainer.trainModel();
//        modelTrainer.saveModel(savePath);


        DataLoader dataLoader = new DataLoader(toInferencePath);
        Vector<String> data = dataLoader.getEvaluationInsatnces();
        Evaluator evaluator = new Evaluator(data, modelTrainer.getInferencer());
        evaluator.evaluate();


        RaportWriterInterface raportWriter = new SmallRaportWriter();

        raportWriter.writeRaport(evaluator, System.out);
        //inference.close();
    }
}
