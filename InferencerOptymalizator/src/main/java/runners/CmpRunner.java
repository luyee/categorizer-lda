package runners;

import evaluator.DataLoader;
import evaluator.Evaluator;
import evaluator.RaportWriterInterface;
import evaluator.SmallRaportWriter;
import interfaces.AbstractInferencer;
import interfaces.AbstractModelTrainer;
import ldainference.ModelTrainer;
import svminferencer.KnnModelTrainer;
import svminferencer.SVMModelTrainer;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 3/25/12
 * Time: 3:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class CmpRunner {

    private  Vector<String> data;
    private AbstractModelTrainer modelTrainer;
    private AbstractInferencer inferencer;

    public static void main(String args[]) throws Exception {
        new CmpRunner().run();

    }

    public void run() throws Exception {
        String path = "/home/kacper/dev/categorizer-lda/data";
        String[] pref = {"small", "12", "40", "402", "all"};
        int [] numIterations = {1000,2000,10000,10000,10000};
        int [] numTopics = {2,12,40,40,80};
        int [] k = {6,36,36,36,36};
        for (int i = 0; i < pref.length; i++) {
            String trainingFilePath = path + "/" + pref[i] + "/" + pref[i] + "Data.txt";
            String toInferencePath = path + "/" + pref[i] + "/" + pref[i] + "EvalData.txt";
            DataLoader dataLoader = new DataLoader(toInferencePath);
            data = dataLoader.getEvaluationInsatnces();

            System.out.println("set :"+ pref[i]);
            System.err.println("set :" + pref[i]);
            /*
            lda
             */
            modelTrainer = new ModelTrainer(trainingFilePath,numIterations[i],numTopics[i]);

            System.out.println("lda");
            System.err.println("lda");
            runInference();
            modelTrainer.saveModel(path);

            /*
            svm
             */
            System.out.println("svm");
            System.err.println("svm");
            modelTrainer = new SVMModelTrainer(trainingFilePath,numTopics[i]);
            runInference();
            modelTrainer.saveModel(path);


            /*
           knn
            */
            System.out.println("knn");
            System.err.println("knn");
            modelTrainer = new KnnModelTrainer(trainingFilePath,numTopics[i],k[i]);
            runInference();
            modelTrainer.saveModel(path);
        }
    }

    public void runInference() throws Exception {
        modelTrainer.trainModel();

        Evaluator evaluator = new Evaluator(data,modelTrainer.getInferencer());
        evaluator.evaluate();

        RaportWriterInterface raportWriter = new SmallRaportWriter();

        raportWriter.writeRaport(evaluator, System.out);

    }

}
