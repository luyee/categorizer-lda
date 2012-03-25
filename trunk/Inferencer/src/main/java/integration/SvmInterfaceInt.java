package integration;

import interfaces.AbstractInferencer;
import svminferencer.SVMModelTrainer;

import java.util.Arrays;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 3/25/12
 * Time: 2:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class SvmInterfaceInt {

    public static  void main(String[] args) throws Exception {
    String trainingFilePath = "/home/kacper/dev/categorizer-lda/data/small/smallData.txt";
    String toInferencePath = "/home/kacper/dev/categorizer-lda/data/small/smallEvalData.txt";


    SVMModelTrainer svmModelTrainer = new SVMModelTrainer(trainingFilePath,12);
    svmModelTrainer.trainModel();

        AbstractInferencer inf= svmModelTrainer.getInferencer();

        String topic0 =
                "dupa darwin ewolucja teoria gatunek dobór cecha człowiek praca populacja proces to mechanizm badanie czas rok temat";
        String topic1=
                "architektura sztuka modernizm budynek architekt dzieło rok forma gaudí to fotografia rozwój definicja materiał projekt idea czas dom";


        Vector<String> tp0 = new Vector<String>(Arrays.asList(topic0.split("\\s+")));

        inf.inferenceCategories(tp0);

    }
}
