package ldainference;

import cc.mallet.topics.TopicInferencer;
import cc.mallet.types.InstanceList;
import svminferencer.SVMModelTrainer;
import svminferencer.SvmInferencer;
import weka.classifiers.Classifier;

import java.io.File;
import java.util.Arrays;
import java.util.Set;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 3/22/12
 * Time: 8:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class InferencerInterface {

    private Inferencer inferencer;
    private ModelTrainer modelTrainer;
    private SvmInferencer svmInferencer;

    public void train(String trainingCSVPath) throws Exception {
        modelTrainer = new ModelTrainer(trainingCSVPath,100);

        modelTrainer.trainModel();

        File docPerTopic = modelTrainer.getDocPerTopic();
        TopicInferencer ldaInferencer = modelTrainer.getInderencer();
        InstanceList trainingInstanceList = modelTrainer.getTrainingInstanceList();

        SVMModelTrainer svmModelTrainer = new SVMModelTrainer(trainingInstanceList);
        svmModelTrainer.train();


        inferencer = new Inferencer(docPerTopic,ldaInferencer,trainingInstanceList);



        Classifier svm =  svmModelTrainer.getSmo();
        Set<String> categoriesNames = svmModelTrainer.getCategoriesNames();

        svmInferencer = new SvmInferencer(trainingInstanceList,svm,categoriesNames, 2);

    }

    public void run() throws Exception {
        train("/home/kacper/dev/categorizer-lda/data/small/smallData.txt");
        String topic0 =
                "darwin ewolucja teoria gatunek dobór cecha człowiek praca populacja proces to mechanizm badanie czas rok temat";
        String topic1=
                "architektura sztuka modernizm budynek architekt dzieło rok forma gaudí to fotografia rozwój definicja materiał projekt idea czas dom";



        Vector<String> tp0 = new Vector<String>(Arrays.asList(topic0.split("\\s+")));
        Vector<String> tp1 = new Vector<String>(Arrays.asList(topic1.split("\\s+")));

        Vector<String> res = inferencer.inferenceCategories(tp0);


        System.out.println(res.toString());
        //System.out.println(svmInferencer.inferenceCategories(tp0).toString());

        res =   inferencer.inferenceCategories(tp1);


        System.out.println(res.toString());
        System.out.println(svmInferencer.inferenceCategories(tp1).toString());

    }

    public static  void main(String args[]) throws Exception {
         InferencerInterface ii =new InferencerInterface();
         ii.run();


    }

}
